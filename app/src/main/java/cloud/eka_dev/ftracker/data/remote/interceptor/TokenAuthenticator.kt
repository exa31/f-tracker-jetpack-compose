package cloud.eka_dev.ftracker.data.remote.interceptor

import cloud.eka_dev.ftracker.data.local.DataStoreManager
import cloud.eka_dev.ftracker.data.remote.api.AuthService
import cloud.eka_dev.ftracker.data.remote.dto.RefreshRequest
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/*
Authenticator di OkHttp adalah mekanisme otomatis untuk memperbarui kredensial ketika server membalas 401 Unauthorized.

Kalau token expired, Authenticator akan:

1️⃣ Mendapat respon 401 dari server
2️⃣ Melakukan refresh token
3️⃣ Menyimpan token baru
4️⃣ Mengulangi request yang gagal dengan access token yang sudah diperbarui
5️⃣ Semua ini dilakukan otomatis, tanpa perlu kamu handle secara manual di setiap request

🧠 Bedanya Interceptor vs Authenticator
Fitur	Interceptor	Authenticator
Kapan dieksekusi	Setiap request dan response	Hanya ketika server kirim 401
Cocok untuk	Menambah Header / Logging / Encryption	Refresh Token otomatis
Risiko infinite loop	Tinggi kalau salah logic	Rendah karena bawaan sudah dicegah
Threading	Bagian dari request chain	Jalan di background
🔥 Ilustrasi Alur Authenticator
Request → Token Expired → Server kirim 401 → Authenticator aktif →
Refresh Token → Aplikasi update token → Request diulang → Sukses 🎯

🎯 Kenapa lebih baik pakai Authenticator?

Tanpa authenticator (pakai interceptor):

❌ Kamu harus handle error token expired di setiap request
❌ Berpotensi deadlock kalau pakai runBlocking
❌ Bisa retry berkali-kali tanpa kontrol → infinite loop

Dengan authenticator:

✔ Semua refresh token dilakukan otomatis
✔ Bersih, terpusat
✔ Aman dari infinite retry
✔ Tidak mengganggu UI thread

Singkatnya

Authenticator adalah “helper OkHttp yang memperbaiki otentikasi gagal tanpa campur tanganmu”.
 */
class TokenAuthenticator(
    private val dataStore: DataStoreManager,
    private val authService: AuthService
) : Authenticator {

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        val currentToken = runBlocking { dataStore.getToken() }

        // Jika token sudah berubah berarti refresh sudah dilakukan oleh thread lain
        val requestToken = response.request.header("Authorization")?.removePrefix("Bearer ")
        if (currentToken != null && currentToken != requestToken) {
            return response.request.newBuilder()
                .header("Authorization", "Bearer $currentToken")
                .build()
        }

        val refreshToken = runBlocking { dataStore.getRefreshToken() }
        if (refreshToken.isNullOrBlank()) return null

        val refreshCall = authService.refreshToken(RefreshRequest(refreshToken)).execute()
        if (!refreshCall.isSuccessful) return null

        val body = refreshCall.body() ?: return null
        val newToken = body.data

        runBlocking {
            dataStore.saveToken(newToken.accessToken, newToken.refreshToken)
        }

        return response.request.newBuilder()
            .header("Authorization", "Bearer ${newToken.accessToken}")
            .build()
    }
}