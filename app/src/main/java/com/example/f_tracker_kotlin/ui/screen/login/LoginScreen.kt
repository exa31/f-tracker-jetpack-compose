package com.example.f_tracker_kotlin.ui.screen.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.f_tracker_kotlin.ui.navigation.NavRoute

@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit,
    vm: LoginViewModel = hiltViewModel()
) {
    val GreenPrimary = Color(0xFF4ADE80)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    fun validateEmail(): Boolean {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailError = "Email tidak valid"
            false
        } else {
            emailError = null
            true
        }
    }

    fun validatePassword(): Boolean {
        return if (password.length < 6) {
            passwordError = "Minimal 6 karakter"
            false
        } else {
            passwordError = null
            true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1F)) // background gelap
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Welcome Back 👋",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
            Text(
                "Masuk untuk melanjutkan",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFFAAAAAA) // sedikit abu-abu
                )
            )

            Spacer(Modifier.height(40.dp))

            // EMAIL
            OutlinedTextField(
                textStyle = TextStyle(color = Color.White),
                value = email,
                onValueChange = {
                    email = it
                    validateEmail()
                },
                label = { Text("Email") },
                singleLine = true,
                isError = emailError != null,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Gray,     // label default
                    focusedBorderColor = Color(0xFF4ADE80), // hijau #4ade80 saat fokus
                    unfocusedBorderColor = Color.Gray,       // border default
                    focusedLabelColor = Color(0xFF4ADE80),   // label hijau saat fokus
                    cursorColor = Color(0xFF4ADE80),         // kursor hijau
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (validateEmail()) passwordFocusRequester.requestFocus()
                    }
                )
            )
            emailError?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // PASSWORD
            OutlinedTextField(
                textStyle = TextStyle(color = Color.White),
                value = password,
                onValueChange = {
                    password = it
                    validatePassword()
                },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (validateEmail() && validatePassword())
                            vm.login(email, password, onLoginSuccess)
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Gray,     // label default
                    focusedBorderColor = Color(0xFF4ADE80), // hijau #4ade80 saat fokus
                    unfocusedBorderColor = Color.Gray,       // border default
                    focusedLabelColor = Color(0xFF4ADE80),   // label hijau saat fokus
                    cursorColor = Color(0xFF4ADE80),         // kursor hijau
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                )
            )
            passwordError?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = MaterialTheme.typography.labelSmall.fontSize,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )
            }

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFAAAAAA)
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = GreenPrimary,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        if (!loading) navController.navigate(route = NavRoute.Register.route) {
                            popUpTo(NavRoute.Login.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            Spacer(Modifier.height(28.dp))

            Button(
                onClick = {
                    if (validateEmail() && validatePassword()) {
                        vm.login(email, password, onLoginSuccess)
                    }
                },
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp),
                        color = Color.White
                    )
                } else {
                    Text("Login")
                }
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
