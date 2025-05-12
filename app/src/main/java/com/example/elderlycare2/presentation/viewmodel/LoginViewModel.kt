package com.example.elderlycare2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.repository.LoginRepository
import com.example.elderlycare2.presentation.state.LoginEvent
import com.example.elderlycare2.presentation.state.LoginState
import com.example.elderlycare2.presentation.state.SignUpState
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnEmailChange -> _loginState.value = _loginState.value.copy(email = event.email)
            is LoginEvent.OnPasswordChange -> _loginState.value = _loginState.value.copy(password = event.password)
            is LoginEvent.OnSubmit -> handleLogin(loginState.value.email, loginState.value.password)
            is LoginEvent.LogoutEvent -> logout()
            is LoginEvent.ClearLoginResults -> clearLoginResults()

        }
    }

    private fun handleLogin(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = _loginState.value.copy(isLoading = true, error = null, isSuccess = false)
            try {
                val response = loginRepository.loginUser(email, password)
                val token = response.token // Now this works!
                val role = response.role
                sessionManager.saveAuthToken(token)
                sessionManager.saveUserRole(role)
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    role = role // Set role in state
                )
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(isLoading = false, error = e.message ?: "An unknown error occurred")
            }
        }
    }

    private fun clearLoginResults() {
        _loginState.value = LoginState()
    }

    fun fetchRole(): String? {
        val role = sessionManager.fetchRole()
        return role
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _loginState.value = LoginState()
            _loginState.update { it.copy(logoutSuccess = true) }
        }
    }
}