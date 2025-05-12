package com.example.elderlycare2.presentation.state

data class NurseDeleteState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

sealed class NurseDeleteEvent {
    data class DeleteUser(val userId: String) : NurseDeleteEvent()
    object UserDeleted : NurseDeleteEvent()
    data class ShowError(val message: String) : NurseDeleteEvent()
}