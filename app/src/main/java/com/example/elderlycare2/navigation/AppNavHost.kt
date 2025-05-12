package com.example.elderlycare2.navigation

import ViewDetailScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.elderlycare2.presentation.screens.CareScheduleScreen
import com.example.elderlycare2.presentation.screens.LoginScreen
import com.example.elderlycare2.presentation.screens.NurseListScreen
import com.example.elderlycare2.presentation.screens.NurseProfileScreen
import com.example.elderlycare2.presentation.screens.auth.SignUpScreen
import com.example.elderlycare2.presentation.screens.auth.landingPage.LandingPage
import com.example.elderlycare2.presentation.screens.UserHomeScreen
import com.example.elderlycare2.presentation.screens.UserProfileScreen
import com.example.elderlycare2.presentation.state.LoginEvent
import com.example.elderlycare2.presentation.viewmodel.LoginViewModel
import com.example.elderlycare2.utils.SessionManager
import java.net.URLEncoder

@Composable
fun AppNavHost(navController: NavHostController) {

    val loginViewModel: LoginViewModel = hiltViewModel()
    val loginState = loginViewModel.loginState.collectAsState().value

    LaunchedEffect(loginState.logoutSuccess) {
        if (loginState.logoutSuccess) {
            navController.navigate(NavigationRoutes.landing) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.landing
    ) {
        composable("landing") {
            LandingPage(
                onGetStarted = {
                    navController.navigate(NavigationRoutes.SignUp) {
                        popUpTo(NavigationRoutes.landing) { inclusive = true }
                    }
                }
            )
        }

        composable(NavigationRoutes.SignUp) {
            SignUpScreen(
                navController = navController,
                onLoginClick = { navController.navigate(NavigationRoutes.Login) }
            )
        }

        composable(NavigationRoutes.Login) {
            LoginScreen(
                loginViewModel = loginViewModel,
                navController = navController,
                onSignUpClick = { navController.navigate(NavigationRoutes.SignUp) },
                onLoginSuccess = { role ->
                    when (role) {
                        "nurse" -> navController.navigate(NavigationRoutes.NURSE_HOME) {
                            popUpTo(NavigationRoutes.landing) { inclusive = true }
                        }
                        "user" -> navController.navigate("user_home") {
                            popUpTo(NavigationRoutes.landing) { inclusive = true }
                        }
                        else -> navController.navigate(NavigationRoutes.landing) {
                            popUpTo(NavigationRoutes.landing) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(NavigationRoutes.NURSE_HOME) {
            NurseListScreen(
                loginViewModel = loginViewModel,
                nurseListViewModel = hiltViewModel(),
                navController = navController,
                onProfileClick = { nurseId -> navController.navigate("${NavigationRoutes.NURSE_PROFILE}/$nurseId") },
                onElderClick = { elder ->
                    navController.navigate(
                        "nurse/user/${elder.id}/${URLEncoder.encode(elder.name, "UTF-8")}/${URLEncoder.encode(elder.email, "UTF-8")}/details"
                    )
                }
            )
        }

        composable(
            route = NavigationRoutes.ELDER_DETAILS,
            arguments = listOf(
                navArgument("elderId") { type = NavType.StringType },
                navArgument("elderName") { type = NavType.StringType },
                navArgument("elderEmail") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val elderId = backStackEntry.arguments?.getString("elderId") ?: ""
            val elderName = backStackEntry.arguments?.getString("elderName") ?: ""
            val elderEmail = backStackEntry.arguments?.getString("elderEmail") ?: ""
            ViewDetailScreen(
                elderId = elderId,
                elderName = elderName,
                elderEmail = elderEmail,
                navController = navController,
                loginViewModel = loginViewModel,
            )
        }

        composable(
            route = NavigationRoutes.NURSE_PROFILE,
        )
        {
            NurseProfileScreen(
                navController = navController,
                loginViewModel = loginViewModel,
                onLogoutClick = {
                    loginViewModel.handleEvent(LoginEvent.LogoutEvent)
                }
            )
        }
        composable(
            route = NavigationRoutes.NURSE_TASK,
        )
        {
            CareScheduleScreen(navController = navController)
        }

        composable(NavigationRoutes.USER_HOME) {
            UserHomeScreen(navController = navController, loginViewModel = loginViewModel)
        }

        composable(NavigationRoutes.USER_PROFILE){
            UserProfileScreen(navController = navController, loginViewModel = loginViewModel,onLogoutClick = {
                loginViewModel.handleEvent(LoginEvent.LogoutEvent)
            })
        }
    }
}
