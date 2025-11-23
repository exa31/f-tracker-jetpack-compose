package cloud.eka_dev.ftracker.ui.screen.register

import android.util.Patterns
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cloud.eka_dev.ftracker.ui.navigation.NavRoute

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

    // warna hijau
    val GreenPrimary = Color(0xFF4ADE80)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B1B1F)) // background gelap
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                "Create Account ✨",
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(Modifier.height(32.dp))

            // NAME
            OutlinedTextField(
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4ADE80), // hijau #4ade80 saat fokus
                    unfocusedBorderColor = Color.Gray,       // border default
                    focusedLabelColor = Color(0xFF4ADE80),   // label hijau saat fokus
                    unfocusedLabelColor = Color.Gray,     // label default
                    cursorColor = Color(0xFF4ADE80),         // kursor hijau
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                value = name,
                onValueChange = { name = it; if (name.length >= 3) nameError = null },
                label = { Text("Name") },
                singleLine = true,
                isError = nameError != null,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { if (name.length >= 3) emailFocus.requestFocus() }
                )
            )
            nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(12.dp))

            // EMAIL
            OutlinedTextField(
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Gray,     // label default
                    focusedBorderColor = Color(0xFF4ADE80), // hijau #4ade80 saat fokus
                    unfocusedBorderColor = Color.Gray,       // border default
                    focusedLabelColor = Color(0xFF4ADE80),   // label hijau saat fokus
                    cursorColor = Color(0xFF4ADE80),         // kursor hijau
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error,
                ),
                value = email,
                onValueChange = {
                    email = it; if (Patterns.EMAIL_ADDRESS.matcher(email)
                        .matches()
                ) emailError = null
                },
                label = { Text("Email Address") },
                singleLine = true,
                isError = emailError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { if (emailError == null) passFocus.requestFocus() }
                ),
            )

            emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(12.dp))

            // PASSWORD
            OutlinedTextField(
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Gray,     // label default
                    focusedBorderColor = Color(0xFF4ADE80), // hijau #4ade80 saat fokus
                    unfocusedBorderColor = Color.Gray,       // border default
                    focusedLabelColor = Color(0xFF4ADE80),   // label hijau saat fokus
                    cursorColor = Color(0xFF4ADE80),         // kursor hijau
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                value = password,
                onValueChange = { password = it; if (password.length >= 6) passwordError = null },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                isError = passwordError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { if (passwordError == null) confirmFocus.requestFocus() }
                )
            )
            passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(12.dp))

            // CONFIRM PASSWORD
            OutlinedTextField(
                textStyle = TextStyle(color = Color.White),
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    if (confirmPassword == password) confirmError = null
                },
                label = { Text("Confirm Password") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedLabelColor = Color.Gray,     // label default
                    focusedBorderColor = Color(0xFF4ADE80), // hijau #4ade80 saat fokus
                    unfocusedBorderColor = Color.Gray,       // border default
                    focusedLabelColor = Color(0xFF4ADE80),   // label hijau saat fokus
                    cursorColor = Color(0xFF4ADE80),         // kursor hijau
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorLabelColor = MaterialTheme.colorScheme.error
                ),
                visualTransformation = PasswordVisualTransformation(),
                isError = confirmError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(confirmFocus),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboard?.hide()
                        if (nameError == null && emailError == null && passwordError == null && confirmError == null) {
                            vm.register(name, email, password, onRegisterSuccess)
                        }
                    }
                )
            )
            confirmError?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Spacer(Modifier.height(28.dp))

            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    text = "Already have an account?",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Login",
                    color = GreenPrimary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        if (!loading) navController.navigate(route = NavRoute.Login.route) {
                            popUpTo(NavRoute.Register.route) { inclusive = true }
                        }
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { vm.register(name, email, password, onRegisterSuccess) },
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(22.dp),
                        color = Color.White
                    )
                } else {
                    Text("Register", color = Color.White)
                }
            }

            error?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
