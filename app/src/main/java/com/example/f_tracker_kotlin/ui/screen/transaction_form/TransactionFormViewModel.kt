package com.example.f_tracker_kotlin.ui.screen.transaction_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f_tracker_kotlin.data.enums.TransactionType
import com.example.f_tracker_kotlin.data.remote.dto.BaseResponse
import com.example.f_tracker_kotlin.data.repository.TransactionRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val repo: TransactionRepository,
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage = _snackbarMessage

    fun addTransaction(
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

}