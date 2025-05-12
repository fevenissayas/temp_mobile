package com.example.elderlycare2.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.elderlycare2.presentation.state.SignUpEvent
import com.example.elderlycare2.presentation.viewmodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel = hiltViewModel(),
    onLoginClick: () -> Unit
) {
    val signUpState by signUpViewModel.signupState.collectAsState()

    val backgroundColor = Color(0xFFCAE7E5)
    val primaryColor = Color(0xFF1C6B66)
    val white = Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "Sign Up",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Role Selector
        RoleSelection(signUpViewModel = signUpViewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Input Fields
        TextFieldLabel("Full Name*")
        SignUpTextField(
            label = "Enter Your Name",
            value = signUpState.name,
            onValueChange = { signUpViewModel.handleEvent(SignUpEvent.OnNameChange(it)) }
        )

        TextFieldLabel("Email*")
        SignUpTextField(
            label = "Enter Your Email",
            value = signUpState.email,
            onValueChange = { signUpViewModel.handleEvent(SignUpEvent.OnEmailChange(it)) }
        )

        TextFieldLabel("Password*")
        SignUpTextField(
            label = "Enter your password",
            value = signUpState.password,
            isPassword = true,
            onValueChange = { signUpViewModel.handleEvent(SignUpEvent.OnPassword(it)) }
        )

        TextFieldLabel("Confirm Password*")
        SignUpTextField(
            label = "ReEnter Your password",
            value = signUpState.confirmPassword,
            isPassword = true,
            onValueChange = { signUpViewModel.handleEvent(SignUpEvent.OnConfirmPassword(it)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                if (signUpState.password == signUpState.confirmPassword) {
//                    signUpViewModel.handleEvent(SignUpEvent.OnSubmit)
                } else {
                    signUpViewModel.handleEvent(SignUpEvent.ClearSignupResults)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (signUpState.isLoading) {
                CircularProgressIndicator(color = white, modifier = Modifier.size(24.dp))
            } else {
                Text("Create Account", color = white, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Login Prompt
        Row {
            Text("Already have an account? ")
            Text(
                "Login",
                color = primaryColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Handle Results
        when {
            signUpState.isSignedUp -> {
                Text(
                    text = "Sign up successful!",
                    color = Color.Green,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                signUpViewModel.handleEvent(SignUpEvent.ClearSignupResults)
            }
            signUpState.error != null -> {
                Text(
                    text = signUpState.error!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                signUpViewModel.handleEvent(SignUpEvent.ClearSignupResults)
            }
        }
    }
}

@Composable
fun RoleToggleButton(text: String, selected: Boolean, onClick: () -> Unit) {
    val backgroundColor = if (selected) Color(0xFF1C6B66) else Color.White
    val textColor = if (selected) Color.White else Color.Black

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(140.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TextFieldLabel(label: String) {
    Text(
        text = label,
        color = Color.Black,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun SignUpTextField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        visualTransformation = visualTransformation,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF1C6B66),
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color(0xFF1C6B66),
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White
        )
    )
}

@Composable
fun RoleSelection(signUpViewModel: SignUpViewModel) {
    val signupState by signUpViewModel.signupState.collectAsState()

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        RoleToggleButton(
            text = "User",
            selected = signupState.role == "USER"
        ) {
            signUpViewModel.handleEvent(SignUpEvent.OnRoleChange("USER"))
        }

        RoleToggleButton(
            text = "Nurse",
            selected = signupState.role == "NURSE"
        ) {
            signUpViewModel.handleEvent(SignUpEvent.OnRoleChange("NURSE"))
        }
    }
}