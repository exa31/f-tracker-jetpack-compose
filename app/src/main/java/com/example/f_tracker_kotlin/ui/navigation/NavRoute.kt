package com.example.f_tracker_kotlin.ui.navigation

/*
Itu adalah Sealed Class di Kotlin yang digunakan untuk mendefinisikan route (nama screen) dalam navigasi Jetpack Compose.

📌 Penjelasan
sealed class NavRoute(val route: String) {
    data object Login : NavRoute("login")
    data object Home : NavRoute("home")
}


✔ sealed class → membatasi inheritance sehingga semua class turunannya harus didefinisikan di file yang sama
✔ Dipakai biar navigasi lebih aman dan terstruktur
✔ data object → membuat singleton object untuk setiap route (jadi tidak perlu instance lagi)

🔍 Kenapa tidak pakai langsung string?

Tanpa ini:

navController.navigate("logni") // Typo, compile tidak error tapi navigasi gagal!


Dengan NavRoute:

navController.navigate(NavRoute.Login.route)


➡ If salah penulisan nama route compile langsung error, jadi lebih aman dan terkontrol.

📍 Cara pakai dalam Navigation
NavHost(
    navController = navController,
    startDestination = NavRoute.Login.route
) {
    composable(NavRoute.Login.route) { LoginScreen() }
    composable(NavRoute.Home.route) { HomeScreen() }
}

🎯 Intinya

NavRoute ini seperti enum untuk screen navigation, tapi lebih fleksibel:

Bisa menyimpan parameter route (misal: detail/{id})

Bisa menambah fungsi di dalamnya

Lebih aman dari typo
 */
sealed class NavRoute(val route: String) {
    data object Home : NavRoute("home")
    data object Login : NavRoute("login")
    data object Register : NavRoute("register")
    data object Splash : NavRoute("splash")

}
