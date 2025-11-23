package cloud.eka_dev.ftracker.data.repository

import cloud.eka_dev.ftracker.data.remote.api.AuthService
import cloud.eka_dev.ftracker.data.remote.dto.LoginRequest
import cloud.eka_dev.ftracker.data.remote.dto.LoginWithGoogleRequest
import cloud.eka_dev.ftracker.data.remote.dto.LogoutRequest
import cloud.eka_dev.ftracker.data.remote.dto.RegisterRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: AuthService
) {
    suspend fun login(email: String, password: String) =
        api.login(LoginRequest(email, password))

    suspend fun loginWithGoogle(idToken: String) =
        api.loginWithGoogle(LoginWithGoogleRequest(credential = idToken))

    suspend fun register(name: String, email: String, password: String) =
        api.register(RegisterRequest(name = name, email = email, password = password))

    suspend fun logout(token: String) =
        api.logout(LogoutRequest(token = token))
}
