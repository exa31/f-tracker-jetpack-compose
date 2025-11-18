package com.example.f_tracker_kotlin.ui.screen.register

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.f_tracker_kotlin.ui.navigation.NavRoute

@Composable
fun RegisterScreen(
    navController: NavController,
    onRegisterSuccess: () -> Unit,
    vm: RegisterViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }

    val emailFocus = remember { FocusRequester() }
    val passFocus = remember { FocusRequester() }
    val confirmFocus = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    fun validateName(): Boolean {
        return if (name.length < 3) {
            nameError = "Minimal 3 karakter"
            false
        } else {
            nameError = null
            true
        }
    }

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

    fun validateConfirm(): Boolean {
        return if (confirmPassword != password) {
            confirmError = "Password tidak sama"
            false
        } else {
            confirmError = null
            true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                "Create Account ✨",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(32.dp))

            // NAME
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; validateName() },
                label = { Text("Name") },
                singleLine = true,
                isError = nameError != null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (validateName()) emailFocus.requestFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            if (nameError != null)
                Text(
                    nameError!!, color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )

            Spacer(Modifier.height(12.dp))


            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; validateEmail() },
                label = { Text("Email Address") },
                singleLine = true,
                isError = emailError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (validateEmail()) passFocus.requestFocus()
                    }
                )
            )
            if (emailError != null)
                Text(
                    emailError!!, color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )

            Spacer(Modifier.height(12.dp))


            // PASSWORD
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; validatePassword() },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        if (validatePassword()) confirmFocus.requestFocus()
                    }
                )
            )
            if (passwordError != null)
                Text(
                    passwordError!!, color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )

            Spacer(Modifier.height(12.dp))


            // CONFIRM PASSWORD
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    validateConfirm()
                },
                label = { Text("Password Confirmation") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(confirmFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                        if (validateName() && validateEmail() && validatePassword() && validateConfirm()) {
                            vm.register(name, email, password, onRegisterSuccess)
                        }
                    }
                )
            )
            if (confirmError != null)
                Text(
                    confirmError!!, color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp)
                )

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.clickable {
                        navController.navigate(route = NavRoute.Login.route)
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            // BUTTON
            if (loading) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(22.dp)   // 👈 ukuran ideal agar pas di tengah
                )
            } else {
                Button(
                    onClick = {
                        if (validateName() && validateEmail() && validatePassword() && validateConfirm()) {
                            vm.register(name, email, password, onRegisterSuccess)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) { Text("Register") }
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
