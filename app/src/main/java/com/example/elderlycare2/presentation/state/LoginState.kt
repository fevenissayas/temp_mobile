package com.example.elderlycare2.presentation.state

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
    val logoutSuccess: Boolean = false,
    val loginSuccess: Boolean = false,
    val role: String = ""
)

sealed class LoginEvent {
    data class OnEmailChange(val email: String) : LoginEvent()
    data class OnPasswordChange(val password: String) : LoginEvent()
    object OnSubmit : LoginEvent()
    object ClearLoginResults : LoginEvent()
    object LogoutEvent : LoginEvent()
}
