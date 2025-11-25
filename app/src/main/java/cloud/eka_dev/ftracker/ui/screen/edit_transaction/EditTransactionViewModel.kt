package cloud.eka_dev.ftracker.ui.screen.edit_transaction

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cloud.eka_dev.ftracker.data.enums.TransactionType
import cloud.eka_dev.ftracker.data.remote.dto.BaseResponse
import cloud.eka_dev.ftracker.data.repository.TransactionRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditTransactionViewModel @Inject constructor(
    private val repo: TransactionRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val transactionId: String? = savedStateHandle["transactionId"]

    private val _notFound = MutableStateFlow(false)
    val notFound: StateFlow<Boolean> = _notFound

    private val _date = MutableStateFlow(
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(System.currentTimeMillis())
    )

    val date: StateFlow<String> = _date

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _amount = MutableStateFlow(TextFieldValue(""))
    val amount: MutableStateFlow<TextFieldValue> = _amount

    private val _type = MutableStateFlow("")
    val type: StateFlow<String> = _type

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage

    init {
        println("Transaction ID: $transactionId")
        if (!transactionId.isNullOrEmpty()) {
            println("Masuk get")
            getTransaction()
        } else {
            println("Masuk not found")
            _loading.value = false
            _notFound.value = true
        }
    }

    fun getTransaction() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val transaction = repo.getTransactionDetail(transactionId!!)
                // Handle the retrieved transaction as needed
                val data = transaction.data

                println(transaction)

                _date.value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(
                        data.createdAt
                    )!!
                )
                _description.value = data.description
                _amount.value = TextFieldValue(data.amount.toString())
                _type.value = data.type
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    _notFound.value = true
                } else {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = try {
                        Gson().fromJson(errorBody, BaseResponse::class.java).message
                    } catch (e: Exception) {
                        "An error occurred"
                    }
                    println("HTTP Error: $errorMessage")
                    _snackbarMessage.emit(errorMessage)
                }
            } catch (_: SocketTimeoutException) {
                _snackbarMessage.emit("Request timed out. Please try again.")
            } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
                _snackbarMessage.emit(e.localizedMessage ?: "An error occurred")
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateTransaction(
        date: String,
        description: String,
        type: TransactionType,
        amount: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.createTransaction(
                    createdAt = date,
                    description = description,
                    type = type,
                    amount = amount
                )
                onSuccess()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    Gson().fromJson(errorBody, BaseResponse::class.java).message
                } catch (e: Exception) {
                    "An error occurred"
                }
                println("HTTP Error: $errorMessage")
                _snackbarMessage.emit(errorMessage)
            } catch (_: SocketTimeoutException) {
                _snackbarMessage.emit("Request timed out. Please try again.")
            } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
                _snackbarMessage.emit(e.localizedMessage ?: "An error occurred")
            } finally {
                _loading.value = false
            }

        }


    }

    fun onDateChange(newDate: String) {
        _date.value = newDate
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun onAmountChange(newAmount: TextFieldValue) {
        _amount.value = newAmount
    }

    fun onTypeChange(newType: String) {
        _type.value = newType
    }

}