package com.example.elderlycare2.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.elderlycare2.R
import com.example.elderlycare2.presentation.state.LoginEvent
import com.example.elderlycare2.presentation.viewmodel.LoginViewModel
import com.example.elderlycare2.ui.theme.BackgroundColor
import com.example.elderlycare2.ui.theme.BorderFocusedColor
import com.example.elderlycare2.ui.theme.BorderUnfocusedColor
import com.example.elderlycare2.ui.theme.ButtonTextColor
import com.example.elderlycare2.ui.theme.PrimaryColor
import com.example.elderlycare2.ui.theme.TextColor
import com.example.elderlycare2.ui.theme.TextFieldBackground

@Composable
fun LoginScreen(
    onForgotPassword: () -> Unit = {},
    onSignUpClick: () -> Unit = {},
    loginViewModel: LoginViewModel,
    navController: NavController,
    onLoginSuccess: (role: String) -> Unit,
) {
    val loginState by loginViewModel.loginState.collectAsState()

    LaunchedEffect(loginState.isSuccess) {
        if (loginState.isSuccess) {
            onLoginSuccess(loginState.role)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Welcome Back",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Welcome back! Please enter your details.",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextColor.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(28.dp))

        LoginTextField(
            label = "Enter your email",
            placeholder = "Email",
            value = loginState.email,
            onValueChange = { loginViewModel.handleEvent(LoginEvent.OnEmailChange(it)) }
        )

        LoginTextField(
            label = "Enter your password",
            placeholder = "Password",
            value = loginState.password,
            isPassword = true,
            onValueChange = { loginViewModel.handleEvent(LoginEvent.OnPasswordChange(it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot Password?",
                color = PrimaryColor,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onForgotPassword() }
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Show loading animation when isLoading is true
        if (loginState.isLoading) {
            CircularProgressIndicator(
                color = PrimaryColor,
                modifier = Modifier
                    .size(48.dp)
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            Button(
                onClick = {
                    if (loginState.email.isNotBlank() && loginState.password.isNotBlank()) {
                        loginViewModel.handleEvent(LoginEvent.OnSubmit)
                    } else {
                        loginViewModel.handleEvent(LoginEvent.ClearLoginResults)
                    }
                },
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text("Log in", color = ButtonTextColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text("Don’t have an account? ", color = TextColor)
            Text(
                "Sign up",
                color = PrimaryColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onSignUpClick() }
            )
        }
    }
}

@Composable
fun LoginTextField(
    label: String,
    placeholder: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val visualTransformation =
        if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp)) {
        Text(
            text = label,
            color = TextColor,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            visualTransformation = visualTransformation,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = TextFieldBackground,
                unfocusedContainerColor = TextFieldBackground,
                focusedTextColor = TextColor,
                unfocusedTextColor = TextColor,
                focusedBorderColor = BorderFocusedColor,
                unfocusedBorderColor = BorderUnfocusedColor,
                cursorColor = TextColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
    }
}