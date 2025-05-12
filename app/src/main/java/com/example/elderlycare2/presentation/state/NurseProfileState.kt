package com.example.elderlycare2.presentation.state

data class NurseProfileState (
    val name: String = "",
    val email: String ="",
    val phoneNumber: String = "",
    val userName: String = "",
    val yearsOfExperience: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)

sealed class NurseProfileEvent {
    data class OnNameChange(val name: String) : NurseProfileEvent()
    data class OnEmailChange(val email: String) : NurseProfileEvent()
    data class OnPhoneNumberChange(val phoneNumber: String) : NurseProfileEvent()
    data class OnUserNameChange(val userName: String) : NurseProfileEvent()
    data class OnYearsOfExperienceChange(val yearsOfExperience: String) : NurseProfileEvent()
    object OnSubmit : NurseProfileEvent()
    object FetchNurseProfile : NurseProfileEvent()
}