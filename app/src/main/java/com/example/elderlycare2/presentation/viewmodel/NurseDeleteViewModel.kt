package com.example.elderlycare2.presentation.viewmodel

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.remote.response.NurseDeleteResponse
import com.example.elderlycare2.data.repository.NurseDeleteRepository
import com.example.elderlycare2.presentation.state.NurseDeleteEvent
import com.example.elderlycare2.presentation.state.NurseDeleteState
import com.example.elderlycare2.presentation.state.NurseListEvent
import com.example.elderlycare2.presentation.state.NurseProfileEvent
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NurseDeleteViewModel @Inject constructor(
    private val nurseDeleteRepository: NurseDeleteRepository,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _nurseDeleteState = MutableStateFlow(NurseDeleteState())
    val nurseDeleteState: StateFlow<NurseDeleteState> = _nurseDeleteState

    fun handleEvent(event: NurseDeleteEvent) {
        when (event) {
            is NurseDeleteEvent.DeleteUser -> {
                deleteUser(event.userId)
            }
            else -> {}
        }
    }

    private fun deleteUser(userId: String) {
        viewModelScope.launch {
            _nurseDeleteState.value = NurseDeleteState(isLoading = true)
            try {
                val token = sessionManager.fetchAuthToken()
                val response = nurseDeleteRepository.nurseDeleteUser(token.toString(), userId)
                _nurseDeleteState.value = NurseDeleteState(isLoading = false, successMessage = response.message)

            } catch (e: Exception) {
                _nurseDeleteState.value = NurseDeleteState(isLoading = false, errorMessage = e.message)
            }
        }
    }
}