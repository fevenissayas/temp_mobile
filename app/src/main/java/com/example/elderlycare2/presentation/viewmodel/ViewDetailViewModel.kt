package com.example.elderlycare2.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elderlycare2.data.remote.request.ViewDetailRequest
import com.example.elderlycare2.data.repository.LoginRepository
import com.example.elderlycare2.data.repository.ViewDetailRepository
import com.example.elderlycare2.presentation.state.ViewDetailEvent
import com.example.elderlycare2.presentation.state.ViewDetailState
import com.example.elderlycare2.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.get

@HiltViewModel
class ViewDetailViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val viewDetailRepository: ViewDetailRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _viewDetailState = MutableStateFlow(ViewDetailState())
    val viewDetailState: StateFlow<ViewDetailState> = _viewDetailState

    val userInfo = sessionManager.getUserInfoFromToken()
    val nurseName = userInfo?.get("name")?.toString() ?: "Unknown"
    val userId = userInfo?.get("id")?.toString() ?: "Unknown"

    fun handleEvent(event: ViewDetailEvent) {
        when (event) {
            is ViewDetailEvent.FetchElderDetail -> {
                fetchElderDetail(event.elderId)
            }
            is ViewDetailEvent.OnHeartRateChange -> {
                _viewDetailState.value = _viewDetailState.value.copy(heartRate = event.heartRate)
            }
            is ViewDetailEvent.OnSugarLevelChange -> {
                _viewDetailState.value = _viewDetailState.value.copy(sugarLevel = event.sugarLevel)
            }
            is ViewDetailEvent.OnBloodPressureChange -> {
                _viewDetailState.value = _viewDetailState.value.copy(bloodPressure = event.bloodPressure)
            }
            is ViewDetailEvent.UpdateUserDetail -> {
                updateVisitDetails(event.elderId, event.heartRate, event.bloodPressure, event.sugarLevel)
            }
            else -> {
                println("Unknown event: $event")
            }
        }
    }

    private fun fetchElderDetail(elderId: String) {
        viewModelScope.launch {
            _viewDetailState.value = _viewDetailState.value.copy(isLoading = true, error = null)
            try {
                val token = sessionManager.fetchAuthToken()
                val elderDetail = viewDetailRepository.getVisitDetails(token.toString(), elderId)
                _viewDetailState.value = _viewDetailState.value.copy(
                    isLoading = false,
                    name = elderDetail.name,
                    email = elderDetail.email,
                    careTaker = "Aster Chane",
                    heartRate = if (elderDetail.heartRate.isNullOrBlank()) "75" else elderDetail.heartRate,
                    bloodPressure = if (elderDetail.bloodPressure.isNullOrBlank()) "120/80" else elderDetail.bloodPressure,
                    bloodType = if (elderDetail.bloodType.isNullOrBlank()) "B+" else elderDetail.bloodType,
                    description = if (elderDetail.description.isNullOrBlank()) "Hypertension\nDiabetes Mellitus\nDementia" else elderDetail.description,
                )
            } catch (e: Exception) {
                _viewDetailState.value = _viewDetailState.value.copy(isLoading = false, error = e.message ?: "An unknown error occurred")
            }
        }
    }

    private fun updateVisitDetails(elderId: String, heartRate: String, bloodPressure: String, sugarLevel: String) {
        viewModelScope.launch {
            _viewDetailState.value = _viewDetailState.value.copy(isLoading = true, error = null)
            try {
                val token = sessionManager.fetchAuthToken()
                viewDetailRepository.updateVisitDetails(token.toString(), elderId,
                    ViewDetailRequest(heartRate, bloodPressure, sugarLevel))
                _viewDetailState.value = _viewDetailState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _viewDetailState.value = _viewDetailState.value.copy(isLoading = false, error = e.message ?: "An unknown error occurred")
            }
        }
    }


}