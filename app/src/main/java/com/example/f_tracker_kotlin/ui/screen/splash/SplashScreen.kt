package com.example.f_tracker_kotlin.ui.screen.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.f_tracker_kotlin.ui.view_model.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navToHome: () -> Unit,
    navToLogin: () -> Unit,
    authViewModel: AuthViewModel
) {
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    // Animasi alpha / fade in
    val alphaAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Animasi fade-in teks "FTracker"
        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500)
        )
        // Tunggu sebentar supaya animasi kelihatan
        delay(500)

        // Tunggu sampai DataStore selesai baca token
        while (isLoggedIn == null) {
            delay(100)
        }

        // Navigasi
        if (isLoggedIn == true) {
            navToHome()
        } else {
            navToLogin()
        }
    }

    // UI Splash
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1F)), // background gelap
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "FTracker",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.alpha(alphaAnim.value)
        )
    }
}
