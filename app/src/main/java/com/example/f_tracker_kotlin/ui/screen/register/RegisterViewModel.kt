package com.example.f_tracker_kotlin.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f_tracker_kotlin.data.local.DataStoreManager
import com.example.f_tracker_kotlin.data.remote.dto.BaseResponse
import com.example.f_tracker_kotlin.data.repository.AuthRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

/*
Fungsi LoginViewModel Ketika Menggunakan Hilt

Kode kamu:

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val store: DataStoreManager
) : ViewModel() {

1️⃣ @HiltViewModel

Ini memberitahu Hilt bahwa:

"ViewModel ini membutuhkan dependency injection, dan Hilt harus membuat instance-nya."

Hilt akan otomatis membuatkan instance LoginViewModel ketika kamu memanggil:

val vm: LoginViewModel = hiltViewModel()


Tanpa perlu manual ViewModelProvider atau factory.

2️⃣ @Inject constructor

Ini artinya LoginViewModel bisa menerima dependency dari Hilt.
Dependency yang disuntikkan:

AuthRepository

DataStoreManager

Keduanya harus disediakan oleh Hilt di AppModule.

Jadi kamu tidak lagi membuat repo = AuthRepository() secara manual.

Dependency injection menjamin:

satu sumber kebenaran

mudah di-mock untuk unit test

memisahkan UI dari data layer

3️⃣ Apa yang dilakukan fungsi login()?

Fungsi ini menjalankan proses:

✔️ Set loading state
_loading.value = true

✔️ Reset error
_error.value = ""

✔️ Panggil API login
val result = repo.login(email, password)

✔️ Simpan token ke DataStore
store.saveToken(result.token, result.token)


⚠️ Ini tampaknya salah karena saveToken(token, token) → harusnya accessToken + refreshToken
Nanti aku bantu betulkan.

✔️ Callback ke screen bila sukses
onSuccess()

✔️ Handle error & stop loading
catch (e: Exception)
finally { _loading.value = false }

4️⃣ Lalu apa manfaat ViewModel?
📌 Menyimpan state UI (loading, error)

ViewModel menjaga state ketika:

screen recomposition

rotation

proses UI berubah

Compose sendiri stateless, tapi ViewModel membuat UI punya state.

📌 Tidak hilang ketika recomposition

Kalau LoginScreen di-recompose 100x,
ViewModel tetap hidup → state gak hilang.

📌 Menghubungkan UI dengan Repository

ViewModel menjadi jembatan antara:

UI ←→ ViewModel ←→ Repository ←→ API/DataStore


Screen jadi bersih, tidak ada logic berat.

🎯 Kesimpulan

LoginViewModel berfungsi untuk:

✔️ Menangani logic bisnis login

(ambil token, simpan, cek error, dll)

✔️ Menyimpan state UI

(loading, error, hasil login)

✔️ Menghubungkan UI dan Repository

(UI tidak perlu tahu detail API/DataStore)

✔️ Menerima dependency dari Hilt

(tanpa manual membuat repo/datastore)
 */

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val store: DataStoreManager
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun register(name: String, email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _error.value = ""
            _loading.value = true
            try {
                val result = repo.login(email, password)
                store.saveToken(result.data.accessToken, result.data.refreshToken)
                onSuccess()
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, BaseResponse::class.java)
                _error.value = errorResponse.message
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
