package com.example.elderlycare2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.remote.request.NurseProfileRequest
import com.example.elderlycare2.data.repository.NurseProfileRespository
import com.example.elderlycare2.presentation.state.NurseProfileEvent
import com.example.elderlycare2.presentation.state.NurseProfileState
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class NurseProfileViewModel @Inject constructor(
    private val nurseProfileRepository: NurseProfileRespository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _nurseProfileState = MutableStateFlow(NurseProfileState())
    val nurseProfileState: StateFlow<NurseProfileState> = _nurseProfileState

    val userInfo = sessionManager.getUserInfoFromToken()
    val nurseName = userInfo?.get("name")?.toString() ?: "Unknown"
    val userId = userInfo?.get("id")?.toString() ?: "Unknown"

    init {
        handleEvent(NurseProfileEvent.FetchNurseProfile)
    }

    fun handleEvent(event: NurseProfileEvent) {
        when (event) {
            is NurseProfileEvent.FetchNurseProfile -> {
                fetchNurseProfile()
            }
            is NurseProfileEvent.OnNameChange -> {
                _nurseProfileState.value = _nurseProfileState.value.copy(name = event.name)
            }
            is NurseProfileEvent.OnEmailChange -> {
                _nurseProfileState.value = _nurseProfileState.value.copy(email = event.email)
            }
            is NurseProfileEvent.OnPhoneNumberChange -> {
                _nurseProfileState.value = _nurseProfileState.value.copy(phoneNumber = event.phoneNumber)
            }
            is NurseProfileEvent.OnUserNameChange -> {
                _nurseProfileState.value = _nurseProfileState.value.copy(userName = event.userName)
            }
            is NurseProfileEvent.OnYearsOfExperienceChange -> {
                _nurseProfileState.value = _nurseProfileState.value.copy(yearsOfExperience = event.yearsOfExperience)
            }
            is NurseProfileEvent.OnSubmit -> {
                updateProfile()
            }
            else -> {
                println("Unknown event: $event")
            }
        }
    }

    private fun fetchNurseProfile() {
        viewModelScope.launch {
            _nurseProfileState.value = _nurseProfileState.value.copy(isLoading = true, error = null)
            try {
                val token = sessionManager.fetchAuthToken()
                val nurseProfile = nurseProfileRepository.getNurseProfile(token.toString())
                _nurseProfileState.value = _nurseProfileState.value.copy(
                    isLoading = false,
                    name = nurseProfile.data.name,
                    email = nurseProfile.data.email,
                    phoneNumber = if (nurseProfile.data.phoneNo.isNullOrEmpty()) "+251 911 251 191" else nurseProfile.data.phoneNo,
                    userName = "@${nurseProfile.data.name.lowercase()}",
                    yearsOfExperience = "+${Random.nextInt(1, 6)} years"
                )
            } catch (e: Exception) {
                _nurseProfileState.value = _nurseProfileState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    private fun updateProfile() {
        viewModelScope.launch {
            _nurseProfileState.value = _nurseProfileState.value.copy(isLoading = true, error = null)
            try {
                val token = sessionManager.fetchAuthToken()
                nurseProfileRepository.updateNurseProfile(
                    token.toString(),
                    NurseProfileRequest(
                        _nurseProfileState.value.name,
                        _nurseProfileState.value.email,
                        _nurseProfileState.value.phoneNumber,
                        _nurseProfileState.value.userName
                    )
                )
                _nurseProfileState.value = _nurseProfileState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _nurseProfileState.value = _nurseProfileState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }
}