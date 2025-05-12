package com.example.elderlycare2.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.elderlycare2.presentation.viewmodel.NurseProfileViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.elderlycare2.navigation.NavigationRoutes
import com.example.elderlycare2.presentation.component.BottomNavBar
import com.example.elderlycare2.presentation.state.LoginEvent
import com.example.elderlycare2.presentation.state.NurseProfileEvent
import com.example.elderlycare2.presentation.viewmodel.LoginViewModel

@Composable
fun NurseProfileScreen(
    navController: NavController,
    nurseProfileViewModel: NurseProfileViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel,
    onLogoutClick: () -> Unit = {}
) {
    val loginState by loginViewModel.loginState.collectAsState()
    // ... your other code ...

    if (loginState.logoutSuccess) {
        // Navigate to login (replace "login" with your actual route)
        LaunchedEffect(Unit) {
            navController.navigate(NavigationRoutes.landing) {
                popUpTo(0) // or your login screen route
                launchSingleTop = true
            }
            // Now reset the flag so it works next time
            loginViewModel.handleEvent(LoginEvent.ClearLoginResults)
        }
    }
    var isEditing by remember { mutableStateOf(false) }

    val nurseProfileState by nurseProfileViewModel.nurseProfileState.collectAsState()

    // ADD: Fetch profile on first composition
    LaunchedEffect(Unit) {
        nurseProfileViewModel.handleEvent(NurseProfileEvent.FetchNurseProfile)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController, showUpload = false, loginViewModel = loginViewModel)
        }
    ) { padding ->
        if (nurseProfileState.isLoading) {
            // Show loading spinner centered on the screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (nurseProfileState.error != null) {
            // Show error message
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nurseProfileState.error ?: "Unknown error",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // Show profile content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Profile Info Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Nurse, ${nurseProfileState.name}", fontWeight = FontWeight.SemiBold)
                        Text("ID : ${nurseProfileViewModel.userId}", fontSize = 12.sp)
                        Row {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Star",
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Profile Details Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFCAE7E5)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Full Name", fontWeight = FontWeight.Bold)
                            TextButton(onClick = {
                                if (isEditing) {
                                    nurseProfileViewModel.handleEvent(NurseProfileEvent.OnSubmit)
                                }
                                isEditing = !isEditing
                            }) {
                                Text(if (isEditing) "Done" else "Edit")
                            }
                        }
                        if (isEditing) {
                            TextField(
                                value = nurseProfileState.name,
                                onValueChange = { nurseProfileViewModel.handleEvent(NurseProfileEvent.OnNameChange(it)) }
                            )
                        } else {
                            Text(text = nurseProfileState.name)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Email Address", fontWeight = FontWeight.Bold)
                        if (isEditing) {
                            TextField(
                                value = nurseProfileState.email,
                                onValueChange = { nurseProfileViewModel.handleEvent(NurseProfileEvent.OnEmailChange(it)) }
                            )
                        } else {
                            Text(text = nurseProfileState.email)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Phone number", fontWeight = FontWeight.Bold)
                        if (isEditing) {
                            TextField(
                                value = nurseProfileState.phoneNumber,
                                onValueChange = { nurseProfileViewModel.handleEvent(NurseProfileEvent.OnPhoneNumberChange(it)) }
                            )
                        } else {
                            Text(text = nurseProfileState.phoneNumber)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Username", fontWeight = FontWeight.Bold)
                        if (isEditing) {
                            TextField(
                                value = nurseProfileState.userName,
                                onValueChange = { nurseProfileViewModel.handleEvent(NurseProfileEvent.OnUserNameChange(it)) }
                            )
                        } else {
                            Text(text = nurseProfileState.userName)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Years of Expertise", fontWeight = FontWeight.Bold)
                        if (isEditing) {
                            TextField(
                                value = nurseProfileState.yearsOfExperience,
                                onValueChange = { nurseProfileViewModel.handleEvent(NurseProfileEvent.OnYearsOfExperienceChange(it)) }
                            )
                        } else {
                            Text(text = nurseProfileState.yearsOfExperience)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))

                // Logout Button
                Button(
                    onClick = {
                        loginViewModel.handleEvent(LoginEvent.LogoutEvent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Text("Logout", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}