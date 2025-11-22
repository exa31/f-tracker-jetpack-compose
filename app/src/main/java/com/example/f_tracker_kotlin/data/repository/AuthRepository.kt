package com.example.f_tracker_kotlin.data.repository

import com.example.f_tracker_kotlin.data.remote.api.AuthService
import com.example.f_tracker_kotlin.data.remote.dto.LoginRequest
import com.example.f_tracker_kotlin.data.remote.dto.LogoutRequest
import com.example.f_tracker_kotlin.data.remote.dto.RegisterRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthService
) {
    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun register(name: String, email: String, password: String) =
        api.register(RegisterRequest(name = name, email = email, password = password))

    suspend fun logout(token: String) =
        api.logout(LogoutRequest(token = token))
}
