package com.example.f_tracker_kotlin.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


/*
Bagian ini adalah tempat menyimpan token login secara persistent.
Anggap dia seperti loker kecil untuk menyimpan data yang harus tetap ada walaupun aplikasi ditutup.

🧱 Baris per Baris Penjelasan
1. Membuat extension dataStore untuk Context
val Context.dataStore by preferencesDataStore("auth_store")


Ini menambahkan properti .dataStore ke semua Context.
"auth_store" = nama file penyimpanan.

Hasilnya nanti bikin file kecil bernama:

auth_store.preferences_pb


di internal storage aplikasi.

2. DataStoreManager adalah kartu akses ke loker tadi
class DataStoreManager(private val context: Context)


Dari Hilt, kita inject Context supaya bisa akses dataStore.

3. Ini kunci lemari buat field Token
private val TOKEN_KEY = stringPreferencesKey("token_key")


Bayangkan:
TOKEN_KEY = nama slot penyimpanan.

4. Menyimpan Token
suspend fun saveToken(token: String) {
    context.dataStore.edit { prefs ->
        prefs[TOKEN_KEY] = token
    }
}


edit = buka lemari.

prefs[TOKEN_KEY] = token = taruh token.

Karena I/O, harus suspend.

5. Mengambil Token
suspend fun getToken(): String? {
    val data = context.dataStore.data.first()
    return data[TOKEN_KEY]
}


data.first() mengambil data pertama yang tersedia (flow → value).

Kembalikan token.

🎒 Singkatnya:
Fungsi	Tugas	Dipakai kapan
saveToken()	Menyimpan token login	Setelah login sukses ✅
getToken()	Mengambil token	Saat butuh otorisasi API, cek login, dll ✅
🔥 Contoh Pemakaian Nyata

ViewModel setelah login:

store.saveToken(result.token)


Saat membuka home:

val token = store.getToken()
if (token == null) navigateToLogin()

🌟 Intuisi Simple

Retrofit → buat request → dapet token.

DataStore → simpan token supaya tidak hilang.

ViewModel → yang ngatur alur login / logout.


Kalau datanya makin banyak, tidak disarankan pakai Preferences DataStore.

Kenapa?

Karena preferencesDataStore hanya cocok untuk:

token

setting user

sesuatu yang sederhana

Kalau data user banyak → gunakan Proto DataStore
(jadi disimpan dalam object / class, bukan per key).



 */

val Context.dataStore by preferencesDataStore("auth_store")

class DataStoreManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token_key")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token_key")
    }

    suspend fun saveToken(token: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    suspend fun getToken(): String? {
        val data = context.dataStore.data.first()
        return data[TOKEN_KEY]
    }

    suspend fun getRefreshToken(): String? {
        val data = context.dataStore.data.first()
        return data[REFRESH_TOKEN_KEY]
    }

    suspend fun clearTokens() {
        context.dataStore.edit { prefs ->
            prefs.remove(TOKEN_KEY)
            prefs.remove(REFRESH_TOKEN_KEY)
        }
    }
}
