package com.example.elderlycare2.data.remote.response

data class NurseProfileData (
    val name: String,
    val id: String,
    val email: String,
    val assignedElders: List<String>,
    val phoneNo: String,
    val address: String,
    val dateOfBirth: String,
)