package com.example.elderlycare2.navigation

object NavigationRoutes {
    const val SignUp = "sign_up"
    const val Login = "login"
    const val landing = "landing"
    // Nurse
    const val NURSE_HOME = "nurse/home"
    const val NURSE_TASK = "nurse/task"
    const val NURSE_PROFILE = "nurse/profile"
    const val ELDER_DETAILS = "nurse/user/{elderId}/{elderName}/{elderEmail}/details"

    // User
    const val USER_PROFILE = "user/profile"
    const val USER_HOME = "user_home"
}