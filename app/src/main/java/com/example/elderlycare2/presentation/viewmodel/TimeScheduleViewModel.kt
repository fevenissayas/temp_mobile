package com.example.elderlycare2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.repository.ScheduleRepository
import com.example.elderlycare2.presentation.state.TimeScheduleEvent
import com.example.elderlycare2.presentation.state.TimeScheduleState
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeScheduleViewModel @Inject constructor(
    private val scheduleRepository: ScheduleRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(TimeScheduleState())
    val state: StateFlow<TimeScheduleState> = _state

    init {
        onEvent(TimeScheduleEvent.FetchQueues)
    }

    private fun onEvent(event: TimeScheduleEvent) {
        when (event) {
            is TimeScheduleEvent.FetchQueues -> {
                fetchTasks()
            }
        }
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, tasks = emptyList())
            try {
                val token = sessionManager.fetchAuthToken()
                val tasks = scheduleRepository.getTasks(token.toString())
                _state.value = _state.value.copy(isLoading = false, tasks = tasks)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message ?: "An unknown error occurred")
            }
        }
    }
}