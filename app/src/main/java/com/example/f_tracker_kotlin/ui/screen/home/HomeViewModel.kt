package com.example.f_tracker_kotlin.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f_tracker_kotlin.data.local.DataStoreManager
import com.example.f_tracker_kotlin.data.model.Transaction
import com.example.f_tracker_kotlin.data.model.ViewOptions
import com.example.f_tracker_kotlin.data.repository.AuthRepository
import com.example.f_tracker_kotlin.data.repository.TransactionRepository
import com.example.f_tracker_kotlin.utils.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private val _dataByDate = MutableStateFlow<Map<String, List<Transaction>>>(emptyMap())
    val dataByDate: StateFlow<Map<String, List<Transaction>>> = _dataByDate

    private val _income = MutableStateFlow(0L)
    val income: StateFlow<Long> = _income

    private val _expanse = MutableStateFlow(0L)
    val expanse: StateFlow<Long> = _expanse


    private val _loadingProggres = MutableStateFlow(false)
    val loadingProggress: StateFlow<Boolean> = _loadingProggres

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

    fun getTransactions() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val transactions = transactionRepo.getTransactions(ViewOptions.MONTH)
                // Handle transactions
                val mappedCurrent = Transaction.fromDtoCurrent(data = transactions.data)
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


            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }

    }

}