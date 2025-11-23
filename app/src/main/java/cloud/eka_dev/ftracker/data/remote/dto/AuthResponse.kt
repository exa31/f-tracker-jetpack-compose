package cloud.eka_dev.ftracker.data.remote.dto

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
)
