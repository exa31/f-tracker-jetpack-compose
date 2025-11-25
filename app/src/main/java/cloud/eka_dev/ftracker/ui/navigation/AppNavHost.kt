package cloud.eka_dev.ftracker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cloud.eka_dev.ftracker.data.connectivity.ConnectivityObserver
import cloud.eka_dev.ftracker.ui.screen.add_transaction.AddTransactionScreen
import cloud.eka_dev.ftracker.ui.screen.edit_transaction.EditTransactionScreen
import cloud.eka_dev.ftracker.ui.screen.home.HomeScreen
import cloud.eka_dev.ftracker.ui.screen.login.LoginScreen
import cloud.eka_dev.ftracker.ui.screen.no_internet.NoInternetBanner
import cloud.eka_dev.ftracker.ui.screen.register.RegisterScreen
import cloud.eka_dev.ftracker.ui.screen.splash.SplashScreen
import cloud.eka_dev.ftracker.ui.view_model.AuthViewModel

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
fun AppNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    connectivityObserver: ConnectivityObserver
) {

    val status by connectivityObserver.observe()
        .collectAsState(initial = ConnectivityObserver.Status.Unavailable)

    Box {
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

            composable(NavRoute.AddTransaction.route) {
                AddTransactionScreen(
                    onSuccess = {
                        navController.navigate(NavRoute.Home.route) {
                            popUpTo(NavRoute.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onClose = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = NavRoute.EditTransaction.route, arguments = listOf(
                    navArgument("transactionId") {
                        defaultValue = ""
                        type = androidx.navigation.NavType.StringType
                        nullable = false
                    }
                )
            ) {
                EditTransactionScreen(
                    onSuccess = {
                        navController.navigate(NavRoute.Home.route) {
                            popUpTo(NavRoute.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                    onClose = {
                        navController.popBackStack()
                    }
                )

            }

            composable(NavRoute.Home.route) {
                HomeScreen(
                    onAddClick = {
                        println("Add Transaction Clicked")
                        navController.navigate(NavRoute.AddTransaction.route)
                    },
                    onEditClick = {
                        println("Edit Transaction Clicked")
                        navController.navigate(NavRoute.EditTransaction.createRoute(transactionId = it._id))
                    },
                    onLogoutSuccess = {
                        navController.navigate(NavRoute.Login.route) {
                            popUpTo(NavRoute.Home.route) {
                                inclusive = true
                            }
                        }
                    },
                )
            }
        }

        if (status != ConnectivityObserver.Status.Available) {
            NoInternetBanner(
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )


        }
    }
}
