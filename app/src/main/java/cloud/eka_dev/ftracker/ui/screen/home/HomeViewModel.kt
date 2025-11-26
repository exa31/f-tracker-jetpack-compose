package cloud.eka_dev.ftracker.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.eka_dev.ftracker.data.enums.ViewOptions
import cloud.eka_dev.ftracker.data.local.DataStoreManager
import cloud.eka_dev.ftracker.data.model.Transaction
import cloud.eka_dev.ftracker.data.remote.dto.BaseResponse
import cloud.eka_dev.ftracker.data.remote.dto.TransactionResponse
import cloud.eka_dev.ftracker.data.repository.AuthRepository
import cloud.eka_dev.ftracker.data.repository.TransactionRepository
import cloud.eka_dev.ftracker.utils.formatDate
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.awaitResponse
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val transactionRepo: TransactionRepository,
    private val store: DataStoreManager
) : ViewModel() {
    // ViewModel logic here

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading
    private val _data = MutableStateFlow<List<Transaction>>(emptyList())
    val data: StateFlow<List<Transaction>> = _data

    private val _selectedView = MutableStateFlow(ViewOptions.MONTH)
    val selectedView: StateFlow<ViewOptions> = _selectedView

    private val _dataByDate = MutableStateFlow<Map<String, List<Transaction>>>(emptyMap())
    val dataByDate: StateFlow<Map<String, List<Transaction>>> = _dataByDate

    private val _income = MutableStateFlow(0L)
    val income: StateFlow<Long> = _income

    private val _expanse = MutableStateFlow(0L)
    val expanse: StateFlow<Long> = _expanse

    private val _id = MutableStateFlow<String?>(null)
    val id: StateFlow<String?> = _id

    private val _loadingProggres = MutableStateFlow(false)
    val loadingProggress: StateFlow<Boolean> = _loadingProggres

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _percentageIncome = MutableStateFlow(0)
    val percentageIncome: StateFlow<Int> = _percentageIncome

    private val _percentageExpanse = MutableStateFlow(0)
    val percentageExpanse: StateFlow<Int> = _percentageExpanse

    private val _isUpTrandIncome = MutableStateFlow(false)
    val isUpTrandIncome: StateFlow<Boolean> = _isUpTrandIncome

    private val _isUpTrandExpanse = MutableStateFlow(false)
    val isUpTrandExpanse: StateFlow<Boolean> = _isUpTrandExpanse

    private val _snackbarMessage = MutableSharedFlow<String>()

    val snackbarMessage = _snackbarMessage
    private var currentCallTransaction: Call<BaseResponse<TransactionResponse>>? = null
    private var currentJob: Job? = null


    init {
        getTransactions()
    }

    fun logOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loadingProggres.value = true
            val token = store.getToken() ?: ""
            try {
                authRepo.logout(token)
                store.clearTokens()
            } catch (e: Exception) {
                store.clearTokens()
                e.printStackTrace()
            } finally {
                _loadingProggres.value = false
                onSuccess()
            }
        }
    }

    fun getTransactions(viewOptions: ViewOptions = ViewOptions.MONTH) {

        // Cancel job lama
        currentJob?.cancel()

        // Cancel network call lama
        currentCallTransaction?.cancel()

        currentJob = viewModelScope.launch {
            _loading.value = true

            try {
                // Ambil call baru
                val call = transactionRepo.getTransactionsCall(viewOptions)
                currentCallTransaction = call

                // Jalankan call secara suspend
                val response = call.awaitResponse()

                if (response.isSuccessful) {
                    val body = response.body()!!
                    val mappedCurrent =
                        Transaction.fromDtoCurrent(data = body.data)

                    _data.value = mappedCurrent
                    _dataByDate.value = mappedCurrent.groupBy { formatDate(it.createdAt) }

                    val incomeTotal = mappedCurrent
                        .filter { it.type.lowercase() == "income" }
                        .sumOf { it.amount }
                    val expanseTotal = mappedCurrent
                        .filter { it.type.lowercase() == "expanse" }
                        .sumOf { it.amount }
                    _income.value = incomeTotal.toLong()
                    _expanse.value = expanseTotal.toLong()

                    val mappedLast = Transaction.fromDtoLast(data = body.data)

                    val lastIncomeTotal = mappedLast
                        .filter { it.type.lowercase() == "income" }
                        .sumOf { it.amount }
                    val lastExpanseTotal = mappedLast
                        .filter { it.type.lowercase() == "expanse" }
                        .sumOf { it.amount }

                    // Calculate percentage changes
                    if (lastIncomeTotal == 0) {
                        _percentageIncome.value = if (incomeTotal == 0) 0 else 100
                        _isUpTrandIncome.value = true
                    } else {
                        val incomeChange = incomeTotal - lastIncomeTotal
                        val incomePercentage =
                            (incomeChange.toDouble() / lastIncomeTotal.toDouble()) * 100
                        _percentageIncome.value = incomePercentage.toInt()
                        _isUpTrandIncome.value = incomeChange >= 0
                    }

                    if (lastExpanseTotal == 0) {
                        _percentageExpanse.value = if (expanseTotal == 0) 0 else 100
                        _isUpTrandExpanse.value = true
                    } else {
                        val expanseChange = expanseTotal - lastExpanseTotal
                        val expansePercentage =
                            (expanseChange.toDouble() / lastExpanseTotal.toDouble()) * 100
                        _percentageExpanse.value = expansePercentage.toInt()
                        _isUpTrandExpanse.value = expanseChange >= 0
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        Gson().fromJson(errorBody, BaseResponse::class.java).message
                    } catch (e: Exception) {
                        "An error occurred"
                    }
                    println("HTTP Error: $errorMessage")
                    _snackbarMessage.emit(errorMessage)
                }
            } catch (e: SocketTimeoutException) {
                _snackbarMessage.emit("Request timed out. Please try again.")
            } catch (e: IOException) {
                _snackbarMessage.emit("Network error occurred")
            } catch (e: CancellationException) {
                Log.d("HomeVM", "Request canceled")
            } catch (e: Exception) {
                e.printStackTrace()
                _snackbarMessage.emit("Error: ${e.message}")
            } finally {
                _loading.value = false
                _isRefreshing.value = false
            }
        }
    }

    fun onViewOptionChange(viewOptions: ViewOptions) {
        _selectedView.value = viewOptions
        getTransactions(viewOptions)
    }

    fun handleRefresh() {
        _isRefreshing.value = true
        getTransactions(_selectedView.value)
    }

    fun deleteTransaction() {
        viewModelScope.launch {
            _loadingProggres.value = true
            val transactionId = _id.value
            try {
                if (transactionId != null) {
                    transactionRepo.deleteTransaction(transactionId)
                    _snackbarMessage.emit("Transaction deleted successfully")
                    getTransactions()
                } else {
                    _snackbarMessage.emit("Invalid transaction ID")
                }
            } catch (e: retrofit2.HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    Gson().fromJson(errorBody, BaseResponse::class.java).message
                } catch (e: Exception) {
                    "An error occurred"
                }
                println("HTTP Error: $errorMessage")
                _snackbarMessage.emit(errorMessage)
            } catch (_: java.net.SocketTimeoutException) {
                _snackbarMessage.emit("Request timed out. Please try again.")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "deleteTransaction: ", e)

            } finally {
                _loadingProggres.value = false
                _id.value = null
            }
        }
    }

    fun onDeleteClick(transaction: Transaction) {
        _id.value = transaction._id
    }

}