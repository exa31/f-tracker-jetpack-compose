package com.example.f_tracker_kotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.f_tracker_kotlin.data.model.Transaction
import com.example.f_tracker_kotlin.ui.screen.home.HomeScreen
import com.example.f_tracker_kotlin.ui.screen.login.LoginScreen
import com.example.f_tracker_kotlin.ui.screen.register.RegisterScreen
import com.example.f_tracker_kotlin.ui.screen.splash.SplashScreen
import com.example.f_tracker_kotlin.ui.view_model.AuthViewModel

/*
AppNavHost Fungsi untuk Navigasi Seluruh Screen
@Composable
fun AppNavHost(navController: NavHostController)


🡆 Ini adalah fungsi Composable yang menjadi tempat utama seluruh routing halaman aplikasi.

NavHostController digunakan untuk pindah halaman (navigate)

Nantinya dipanggil di MainActivity

🔹 Struktur Navigasi
NavHost(navController = navController, startDestination = NavRoute.Login.route)


NavHost = Container navigasi

startDestination = Login → Saat aplikasi pertama kali dibuka, masuk ke halaman Login

🔹 Route Login
composable(NavRoute.Login.route) {
    LoginScreen(
        onLoginSuccess = {
            println("Login Success")
            // nanti diarahkan ke Home
        }
    )
}


composable() = definisi satu halaman

Jika user berhasil login → onLoginSuccess akan dipanggil

Tapi sementara belum navigate ke halaman lain (masih println)

🔹 Route Register
composable(NavRoute.Register.route) {
    RegisterScreen(
        onRegisterSuccess = {
            println("Register Success")
        }
    )
}


Saat masuk ke route "register", maka UI Register akan ditampilkan

Setelah register berhasil → onRegisterSuccess terpanggil

🎯 Kesimpulan
Bagian	Fungsi
NavHost	Tempat buat daftar semua screen
startDestination	Menentukan halaman pertama saat aplikasi buka
composable(route)	Mendefinisikan satu halaman berdasarkan route
navController	Objek untuk pindah antar halaman
Callback seperti onLoginSuccess	Memberitahu navigasi kalau action selesai
Flow Navigasi Saat Ini
Startup → LoginScreen
RegisterScreen → hanya kalau diarahkan manual
Login success → masih belum pindah halaman


Kalau mau flow Login → Home dan Register → Login, kamu tinggal aktifkan:

navController.navigate(NavRoute.Home.route) {
    popUpTo(NavRoute.Login.route) { inclusive = true }
}
 */

@Composable
fun AppNavHost(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {

    NavHost(navController = navController, startDestination = NavRoute.Splash.route) {
        /*
        Berikut parameter penting dari fungsi itu:

        Parameter	Fungsi
        route	Nama rute/halaman
        arguments	Untuk mengirim data lewat navigation (optional)
        deepLinks	Untuk mendukung deep link dari luar aplikasi
        enterTransition	Animasi saat screen masuk
        exitTransition	Animasi saat screen keluar
        popEnterTransition	Animasi saat kembali masuk
        popExitTransition	Animasi saat kembali keluar
        content	Tempat kamu menulis Composable untuk UI screen

        composable("detail/{id}") { backStackEntry ->
        val id = backStackEntry.arguments?.getString("id")
        DetailScreen(id)
        }
         */
        composable(NavRoute.Splash.route) {
            SplashScreen(
                navToHome = {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                navToLogin = {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                authViewModel = authViewModel
            )
        }

        composable(NavRoute.Login.route) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(NavRoute.Register.route) {
            RegisterScreen(
                navController = navController,
                onRegisterSuccess = {
                    navController.navigate(NavRoute.Home.route) {
                        popUpTo(NavRoute.Login.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(NavRoute.Home.route) {
            HomeScreen(
                expense = 1_000_000,
                income = 2_500_000,
                onAddClick = {
                    println("Add Transaction Clicked")
                },
                onDeleteClick = {
                    println("Delete Transaction Clicked")
                },
                onEditClick = {
                    println("Edit Transaction Clicked")
                },
                onLogoutSuccess = {
                    navController.navigate(NavRoute.Login.route) {
                        popUpTo(NavRoute.Home.route) {
                            inclusive = true
                        }
                    }
                },
                transactions = mapOf(
                    "June 22, 2023" to listOf(
                        Transaction(
                            id = 1,
                            amount = 150_000,

                            date = "June 22, 2023",
                            title = "Groceries",
                        ),
                        Transaction(
                            id = 2,
                            amount = 200_000,
                            date = "June 22, 2023",
                            title = "Electricity Bill",
                        ),
                        Transaction(
                            id = 3,
                            amount = 50_000,
                            date = "June 22, 2023",
                            title = "Transport",
                        ),
                        Transaction(
                            id = 4,
                            amount = 50_000,
                            date = "June 12, 2023",
                            title = "Transport",
                        ),
                    )
                )
            )
        }
    }
}
