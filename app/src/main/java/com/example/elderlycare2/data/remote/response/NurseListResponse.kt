package com.example.elderlycare2.data.remote.response

data class NurseListResponse(
    val _id: String,
    val name: String,
    val id: String,
    val email: String,
    val profileImg: String,
    val tasks: List<String>
)