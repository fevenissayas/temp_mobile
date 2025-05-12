package com.example.elderlycare2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.remote.request.AddScheduleRequest
import com.example.elderlycare2.data.remote.response.NurseListResponse
import com.example.elderlycare2.data.repository.CareScheduleRepository
import com.example.elderlycare2.presentation.state.CareScheduleEvent
import com.example.elderlycare2.presentation.state.CareScheduleState
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CareScheduleViewModel @Inject
constructor(
    private val careScheduleRepository: CareScheduleRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _careScheduleState = MutableStateFlow(CareScheduleState())
    val careScheduleState: StateFlow<CareScheduleState> = _careScheduleState

    fun handleEvent(event: CareScheduleEvent) {
        when (event) {
            is CareScheduleEvent.OnCarePlanChange -> _careScheduleState.value = _careScheduleState.value.copy(carePlan = event.carePlan)
            is CareScheduleEvent.OnFrequencyChange -> _careScheduleState.value = _careScheduleState.value.copy(frequency = event.frequency)
            is CareScheduleEvent.OnStartTimeChange -> _careScheduleState.value = _careScheduleState.value.copy(startTime = event.startTime)
            is CareScheduleEvent.OnEndTimeChange -> _careScheduleState.value = _careScheduleState.value.copy(endTime = event.endTime)
            is CareScheduleEvent.OnPostToChange -> _careScheduleState.value = _careScheduleState.value.copy(postTo = event.postTo)
            is CareScheduleEvent.OnSubmit -> handleSubmit()
        }
    }

    private fun handleSubmit() {
        viewModelScope.launch {
            val request = AddScheduleRequest(
                schedule = careScheduleState.value.carePlan,
                frequency = careScheduleState.value.frequency,
                startTime = careScheduleState.value.startTime,
                endTime = careScheduleState.value.endTime,
                assignedTo = careScheduleState.value.postTo
            )
            _careScheduleState.value = _careScheduleState.value.copy(isLoading = true, error = null, isSuccess = false)
            try {
                val token = sessionManager.fetchAuthToken()
                val response = careScheduleRepository.addSchedule(token.toString(), request)
                _careScheduleState.value = _careScheduleState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    error = null
                )
            } catch (e: Exception) {
                _careScheduleState.value = _careScheduleState.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    // Fetch user list for Post To dropdown
    fun fetchUserList() {
        viewModelScope.launch {
            try {
                val token = sessionManager.fetchAuthToken()
                val nurseList: List<NurseListResponse> = careScheduleRepository.fetchUserList(token.toString())
                val userList: List<String> = nurseList.map { it.email }
                _careScheduleState.value = _careScheduleState.value.copy(userList = userList)
            } catch (e: Exception) {
                _careScheduleState.value = _careScheduleState.value.copy(error = "Failed to fetch users: ${e.message}")
            }
        }
    }
}