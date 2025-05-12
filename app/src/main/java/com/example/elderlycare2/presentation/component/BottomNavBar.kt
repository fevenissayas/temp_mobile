package com.example.elderlycare2.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import com.example.elderlycare2.R
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.elderlycare2.navigation.NavigationRoutes
import com.example.elderlycare2.presentation.viewmodel.LoginViewModel

@Composable
fun BottomNavBar(
    navController: NavController,
    loginViewModel: LoginViewModel,
    showUpload: Boolean = true
) {
    var selectedItem by remember { mutableStateOf(0) }
    val role = loginViewModel.fetchRole()
    NavigationBar(
        modifier = Modifier.height(70.dp).border(1.dp, Color.Gray),
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.file),
                    contentDescription = "Documents",
                    modifier = Modifier.size(35.dp)
                )
            },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 1
                if (role == "nurse") {
                    navController.navigate(NavigationRoutes.NURSE_HOME)
                } else {
                    navController.navigate(NavigationRoutes.USER_HOME)
                }
            }
        )

        NavigationBarItem(
            icon = {
                if (showUpload) {
                    Image(
                        painter = painterResource(id = R.drawable.upload),
                        contentDescription = "Upload",
                        modifier = Modifier.size(35.dp)
                    )
                }
            },
            selected = selectedItem == 0,
            onClick = {
                selectedItem = 1
                navController.navigate(NavigationRoutes.NURSE_TASK)
            }
        )

        NavigationBarItem(
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    modifier = Modifier.size(35.dp)
                )
            },
            selected = selectedItem == 0,
            onClick = {
                if (role == "nurse") {
                    navController.navigate(NavigationRoutes.NURSE_PROFILE)
                } else {
                    navController.navigate(NavigationRoutes.USER_PROFILE)
                }
                selectedItem = 0
            }
        )
    }
}