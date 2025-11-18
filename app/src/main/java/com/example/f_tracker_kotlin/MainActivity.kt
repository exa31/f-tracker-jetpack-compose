package com.example.f_tracker_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.example.f_tracker_kotlin.ui.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

/*
1. @AndroidEntryPoint pada MainActivity

Ini wajib karena kamu ingin menggunakan Hilt (Dependency Injection) agar ViewModel, Repository, dan DataStore bisa di-inject otomatis.
Jadi MainActivity seperti ini sudah benar:

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    LoginScreen(
                        onLoginSuccess = {
                            println("Login sukses!")
                        }
                    )
                }
            }
        }
    }
}


✅ Ini cukup untuk tahap awal.
Belum perlu menambahkan ViewModel langsung di MainActivity karena Hilt sudah menyuntikkan ViewModel di dalam LoginScreen.

🧠 2. Apa yang sebenarnya terjadi di balik layar?

Saat kamu menulis @AndroidEntryPoint, Hilt akan:

Membuatkan component graph untuk Activity ini.

Memungkinkan kamu memanggil hiltViewModel() di dalam Composable.

Menyediakan dependensi dari AppModule atau module lain yang kamu buat.

Jadi MainActivity cuma bertugas:

Menampilkan UI (pakai setContent)

Menyediakan konteks ke Hilt

Tidak perlu tahu detil repository atau datastore — itu diurus sama dependency injection

🧰 3. Kalau nanti aplikasi makin besar…

Baru kamu bakal nambah sesuatu di sini, seperti:

Navigation Host (pakai NavHost untuk pindah antar screen)

Scaffold (kalau pakai BottomBar / Drawer)

Theme wrapper (misal FTrackerTheme {} kalau punya custom tema)

TopAppBar atau layout global

Contoh versi “lanjutannya”:

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FTrackerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavHost()
                }
            }
        }
    }
}


Di mana AppNavHost() isinya navigasi Compose, misal LoginScreen, HomeScreen, dsb.

💬 4. Jadi kesimpulannya:
Tahap	Keterangan
✅ Sekarang	Cukup seperti yang kamu tulis — @AndroidEntryPoint + setContent { LoginScreen() }
🚀 Nanti	Bisa ditambah NavHost, Theme, atau layout global
⚠️ Jangan lupa	File App.kt kamu dengan @HiltAndroidApp sudah didaftarkan di AndroidManifest.xml — itu yang membuat Hilt bisa bekerja dari level Application
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    Surface {
                        AppNavHost(navController = navController)
                    }
                }
            }
        }
    }
}
