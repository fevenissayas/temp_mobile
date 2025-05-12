package com.example.elderlycare2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.repository.LoginRepository
import com.example.elderlycare2.data.repository.NurseRepository
import com.example.elderlycare2.presentation.state.NurseListEvent
import com.example.elderlycare2.presentation.state.NurseListState
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NurseListViewModel @Inject constructor(
    private val nurseListRepository: NurseRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow(NurseListState())
    val state: StateFlow<NurseListState> = _state

    init {
        handleEvent(NurseListEvent.FetchElderList)
    }

    fun handleEvent(event: NurseListEvent) {
        when (event) {
            is NurseListEvent.FetchElderList -> {
                fetchElderList()
            }
            else -> {
                println("Unknown event: $event")
            }
        }
    }

    private fun fetchElderList() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null, elderList = emptyList())
            try {
                val token = sessionManager.fetchAuthToken()
                val elderList = nurseListRepository.getUserList(token.toString())
                _state.value = _state.value.copy(isLoading = false, elderList = elderList)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = e.message ?: "An unknown error occurred")
            }
        }
    }

}