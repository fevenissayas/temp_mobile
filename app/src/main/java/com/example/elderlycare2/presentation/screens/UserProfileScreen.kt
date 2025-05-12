package com.example.elderlycare2.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.elderlycare2.R
import com.example.elderlycare2.navigation.NavigationRoutes
import com.example.elderlycare2.presentation.state.UserEditProfileUiEvent
import com.example.elderlycare2.presentation.viewmodel.UserProfileViewModel
import com.example.elderlycare2.presentation.viewmodel.LoginViewModel
import com.example.elderlycare2.presentation.component.BottomNavBar
import com.example.elderlycare2.presentation.state.LoginEvent
import com.example.elderlycare2.ui.theme.BackgroundColor
import com.example.elderlycare2.ui.theme.BackgroundColor1
import com.example.elderlycare2.ui.theme.BackgroundColoruser
import com.example.elderlycare2.ui.theme.BorderFocusedColor
import com.example.elderlycare2.ui.theme.BorderUnfocusedColor
import com.example.elderlycare2.ui.theme.ButtonTextColor

@Composable
fun UserProfileScreen(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel,
    onLogoutClick: () -> Unit = {}
) {
    val loginState by loginViewModel.loginState.collectAsState()
    var isEditing by remember { mutableStateOf(false) }
    if (loginState.logoutSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate(NavigationRoutes.landing) {
                popUpTo(0)
                launchSingleTop = true
            }
            loginViewModel.handleEvent(LoginEvent.ClearLoginResults)
        }
    }
    val state by userProfileViewModel.userProfileState.collectAsState()

    LaunchedEffect(Unit) {
        userProfileViewModel.handleEvent(UserEditProfileUiEvent.LoadProfile)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, showUpload = false, loginViewModel = loginViewModel)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(BackgroundColoruser)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Profile",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(50.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.width(35.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Text(state.fullName, fontSize = 32.sp, fontWeight = FontWeight.SemiBold)
                    Text(state.email, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Profile fields with edit button at the top right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        if (isEditing) {
                            userProfileViewModel.handleEvent(UserEditProfileUiEvent.OnSubmit)
                        }
                        isEditing = !isEditing
                    }
                ) {
                    Text(if (isEditing) "Done" else "Edit")
                }
            }

            ProfileField(
                label = "Full Name",
                value = state.fullName,
                onValueChange = { userProfileViewModel.handleEvent(UserEditProfileUiEvent.UpdateFullName(it)) },
                isEditing = isEditing
            )
            ProfileField(
                label = "Gender",
                value = state.gender,
                onValueChange = { userProfileViewModel.handleEvent(UserEditProfileUiEvent.UpdateGender(it)) },
                isEditing = isEditing
            )
            ProfileField(
                label = "Phone Number",
                value = state.phoneNumber,
                onValueChange = { userProfileViewModel.handleEvent(UserEditProfileUiEvent.UpdatePhoneNumber(it)) },
                isEditing = isEditing
            )
            ProfileField(
                label = "Care Taker",
                value = state.caretaker,
                onValueChange = { userProfileViewModel.handleEvent(UserEditProfileUiEvent.UpdateCaretaker(it)) },
                isEditing = isEditing
            )
            ProfileField(
                label = "Address",
                value = state.address,
                onValueChange = { userProfileViewModel.handleEvent(UserEditProfileUiEvent.UpdateAddress(it)) },
                isEditing = isEditing
            )
            ProfileField(
                label = "Email",
                value = state.email,
                onValueChange = { userProfileViewModel.handleEvent(UserEditProfileUiEvent.UpdateEmail(it)) },
                isEditing = isEditing
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            if (state.isSuccess) {
                Text(
                    text = "Profile updated!",
                    color = Color.Green,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    loginViewModel.handleEvent(LoginEvent.LogoutEvent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = BackgroundColor1),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text("Logout", color = ButtonTextColor)
            }
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEditing: Boolean
) {
    val TextfieldBackground = BackgroundColor
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(top = 10.dp).width(130.dp)) {
            Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Column {
            if (isEditing) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = TextfieldBackground,
                        unfocusedContainerColor = TextfieldBackground,
                        focusedBorderColor = BorderFocusedColor,
                        unfocusedBorderColor = BorderUnfocusedColor
                    ),
                    singleLine = true
                )
            } else {
                Text(
                    value,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .background(TextfieldBackground, RoundedCornerShape(20.dp))
                        .padding(vertical = 13.dp, horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}