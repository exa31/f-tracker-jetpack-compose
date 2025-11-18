package com.example.f_tracker_kotlin.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f_tracker_kotlin.data.local.DataStoreManager
import com.example.f_tracker_kotlin.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val store: DataStoreManager
) : ViewModel() {
    // ViewModel logic here

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun logOut(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            val token = store.getToken() ?: ""
            try {
                authRepo.logout(token)
                store.clearTokens()
            } catch (e: Exception) {
                store.clearTokens()
                e.printStackTrace()
            } finally {
                _loading.value = false
                onSuccess()
            }
        }
    }

}