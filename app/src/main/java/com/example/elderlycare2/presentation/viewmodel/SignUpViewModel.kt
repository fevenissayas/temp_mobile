package com.example.elderlycare2.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.elderlycare2.data.repository.SignUpRepository
import com.example.elderlycare2.presentation.state.SignUpState
import com.example.elderlycare2.presentation.state.SignUpEvent

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpRepository: SignUpRepository
) : ViewModel() {

    private val _signupState = MutableStateFlow(SignUpState())
    val signupState: StateFlow<SignUpState> = _signupState

    fun handleEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnNameChange -> _signupState.update { it.copy(name = event.name) }
            is SignUpEvent.OnPassword -> _signupState.update { it.copy(password = event.password) }
            is SignUpEvent.OnEmailChange -> _signupState.update { it.copy(email = event.email) }
            is SignUpEvent.OnRoleChange -> _signupState.update { it.copy(role = event.role) }
            is SignUpEvent.OnConfirmPassword -> _signupState.update { it.copy(confirmPassword = event.confirmPassword) }
            is SignUpEvent.ClearSignupResults -> clearSignupResults()
            is SignUpEvent.OnSubmit -> {
                if(signupState.value.role == "USER") {
                    signUpUser(signupState.value.email, signupState.value.password, signupState.value.name)
                }
                else {
                    signUpNurse(signupState.value.email, signupState.value.password, signupState.value.name)
                }
            }
            else -> {println("Unknown event: $event")}
        }
    }

    private fun signUpUser(email: String, password: String, name: String) {
        viewModelScope.launch {
            _signupState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            try {
                signUpRepository.signUpUser(email, password, name)
                _signupState.value = SignUpState(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _signupState.value = SignUpState(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    private fun signUpNurse(email: String, password: String, name: String) {
        viewModelScope.launch {
            _signupState.update { it.copy(isLoading = true, error = null, isSuccess = false) }
            try {
                signUpRepository.signUpNurse(email, password, name)
                _signupState.value = SignUpState(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _signupState.value = SignUpState(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    private fun clearSignupResults() {
        _signupState.value = SignUpState()
    }

    private fun updateState(update: (SignUpState) -> SignUpState) {
        val current = _signupState.value ?: SignUpState()
        _signupState.value = update(current)
    }
}
