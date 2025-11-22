package com.example.f_tracker_kotlin.data.remote.api

import com.example.f_tracker_kotlin.data.remote.dto.AuthResponse
import com.example.f_tracker_kotlin.data.remote.dto.BaseResponse
import com.example.f_tracker_kotlin.data.remote.dto.LoginRequest
import com.example.f_tracker_kotlin.data.remote.dto.LogoutRequest
import com.example.f_tracker_kotlin.data.remote.dto.RefreshRequest
import com.example.f_tracker_kotlin.data.remote.dto.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/v1/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<AuthResponse>

    @POST("auth/v1/refresh")
    fun refreshToken(@Body request: RefreshRequest): Call<BaseResponse<AuthResponse>>

    @POST("auth/v1/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse<AuthResponse>

    @POST("auth/v1/logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ): BaseResponse<String>

}
