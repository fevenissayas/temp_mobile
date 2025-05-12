package com.example.elderlycare2.presentation.state

data class CareScheduleState(
    val carePlan: String ="",
    val frequency: String="",
    val startTime: String="",
    val endTime: String="",
    val postTo: String="",
    val userList: List<String> = emptyList<String>(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

sealed class CareScheduleEvent {
    data class OnCarePlanChange(val carePlan: String) : CareScheduleEvent()
    data class OnFrequencyChange(val frequency: String) : CareScheduleEvent()
    data class OnStartTimeChange(val startTime: String) : CareScheduleEvent()
    data class OnEndTimeChange(val endTime: String) : CareScheduleEvent()
    data class OnPostToChange(val postTo: String) : CareScheduleEvent()
    object OnSubmit : CareScheduleEvent()
}