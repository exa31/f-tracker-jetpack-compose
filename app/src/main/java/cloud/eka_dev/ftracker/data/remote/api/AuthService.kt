package cloud.eka_dev.ftracker.data.remote.api

import cloud.eka_dev.ftracker.data.remote.dto.AuthResponse
import cloud.eka_dev.ftracker.data.remote.dto.BaseResponse
import cloud.eka_dev.ftracker.data.remote.dto.LoginRequest
import cloud.eka_dev.ftracker.data.remote.dto.LoginWithGoogleRequest
import cloud.eka_dev.ftracker.data.remote.dto.LogoutRequest
import cloud.eka_dev.ftracker.data.remote.dto.RefreshRequest
import cloud.eka_dev.ftracker.data.remote.dto.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/v1/login")
    suspend fun login(
        @Body request: LoginRequest
    ): BaseResponse<AuthResponse>

    @POST("auth/v1/login-with-google")
    suspend fun loginWithGoogle(
        @Body request: LoginWithGoogleRequest
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
