package com.example.elderlycare2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.remote.request.UserEditProfileRequest
import com.example.elderlycare2.data.repository.UserProfileRepository
import com.example.elderlycare2.presentation.state.UserEditProfileState
import com.example.elderlycare2.presentation.state.UserEditProfileUiEvent
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _userProfileState = MutableStateFlow(UserEditProfileState())
    val userProfileState: StateFlow<UserEditProfileState> = _userProfileState

    val userInfo = sessionManager.getUserInfoFromToken()
    val userName = userInfo?.get("name")?.toString() ?: "Unknown"
    val userId = userInfo?.get("id")?.toString() ?: "Unknown"

    init {
        handleEvent(UserEditProfileUiEvent.LoadProfile)
    }

    fun handleEvent(event: UserEditProfileUiEvent) {
        when (event) {
            is UserEditProfileUiEvent.LoadProfile -> {
                fetchUserProfile()
            }
            is UserEditProfileUiEvent.UpdateFullName -> {
                _userProfileState.value = _userProfileState.value.copy(fullName = event.fullName)
            }
            is UserEditProfileUiEvent.UpdateEmail -> {
                _userProfileState.value = _userProfileState.value.copy(email = event.email)
            }
            is UserEditProfileUiEvent.UpdateGender -> {
                _userProfileState.value = _userProfileState.value.copy(gender = event.gender)
            }
            is UserEditProfileUiEvent.UpdatePhoneNumber -> {
                _userProfileState.value = _userProfileState.value.copy(phoneNumber = event.phoneNumber)
            }
            is UserEditProfileUiEvent.UpdateCaretaker -> {
                _userProfileState.value = _userProfileState.value.copy(caretaker = event.caretaker)
            }
            is UserEditProfileUiEvent.UpdateAddress -> {
                _userProfileState.value = _userProfileState.value.copy(address = event.address)
            }
            is UserEditProfileUiEvent.OnSubmit -> {
                updateProfile()
            }
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            _userProfileState.value = _userProfileState.value.copy(isLoading = true, error = null)
            val token = sessionManager.fetchAuthToken() // Assuming this returns String?

            // --- Explicitly check for null or blank token ---
            if (token.isNullOrBlank()) {
                _userProfileState.value = _userProfileState.value.copy(
                    isLoading = false,
                    error = "Authentication error: Token not available. Please log in again."
                )
                return@launch // Stop if no valid token
            }
            // --- End of explicit check ---

            try {
                // Pass the non-null, non-blank token
                val userProfile = userProfileRepository.getUserProfile(token)
                _userProfileState.value = _userProfileState.value.copy(
                    isLoading = false,
                    fullName = userProfile.data.name ?: "",
                    gender = userProfile.data.gender ?: "",
                    phoneNumber = userProfile.data.phoneNo ?: "",
                    caretaker = userProfile.data.caretaker ?: "",
                    address = userProfile.data.address ?: "",
                    email = userProfile.data.email ?: ""
                    // Reset error on success
                    , error = null
                )
            } catch (e: Exception) {
                // Log the exception for better debugging
                _userProfileState.value = _userProfileState.value.copy(
                    isLoading = false,
                    // Provide a more user-friendly message, but keep details from e.message
                    error = "Failed to load profile: ${e.message ?: "An unknown error occurred"}"
                )
            }
        }
    }


    private fun updateProfile() {
        viewModelScope.launch {
            _userProfileState.value = _userProfileState.value.copy(isLoading = true, error = null)
            try {
                val token = sessionManager.fetchAuthToken()
                userProfileRepository.updateUserProfile(
                    token.toString(),
                    UserEditProfileRequest(
                        name = _userProfileState.value.fullName,
                        email = _userProfileState.value.email,
                        caretaker = _userProfileState.value.caretaker,
                        gender = _userProfileState.value.gender,
                        phoneNo = _userProfileState.value.phoneNumber,
                        address = _userProfileState.value.address
                    )
                )
                _userProfileState.value = _userProfileState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _userProfileState.value = _userProfileState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }
}