package com.example.elderlycare2.presentation.state

data class ViewDetailState (
    val name: String = "",
    val email: String = "",
    val careTaker: String = "",
    val heartRate: String = "",
    val bloodPressure: String = "",
    val bloodType: String = "",
    val sugarLevel: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class ViewDetailEvent {
    data class OnHeartRateChange(val heartRate: String) : ViewDetailEvent()
    data class OnBloodPressureChange(val bloodPressure: String) : ViewDetailEvent()
    data class OnSugarLevelChange(val sugarLevel: String) : ViewDetailEvent()
    data class FetchElderDetail(val elderId: String) : ViewDetailEvent()
    data class UpdateUserDetail(
        val elderId: String,
        val heartRate: String,
        val bloodPressure: String,
        val sugarLevel: String
    ) : ViewDetailEvent()
}