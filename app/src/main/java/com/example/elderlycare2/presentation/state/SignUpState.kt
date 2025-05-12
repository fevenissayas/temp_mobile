package com.example.elderlycare2.presentation.state

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: String = "USER", // Default to "USER" to avoid null/blank role
    val isLoading: Boolean = false,
    val isSignedUp: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)

sealed class SignUpEvent {
    data class OnNameChange(val name: String) : SignUpEvent()
    data class OnEmailChange(val email: String) : SignUpEvent()
    data class OnPassword(val password: String) : SignUpEvent()
    data class OnConfirmPassword(val confirmPassword: String) : SignUpEvent()
    data class SignUpUser(val email: String, val password: String, val name: String) : SignUpEvent()
    data class SignUpNurse(val email: String, val password: String, val name: String) : SignUpEvent()
    data class OnRoleChange(val role: String) : SignUpEvent()
    object OnSubmit : SignUpEvent()
    object ClearSignupResults : SignUpEvent()
}
