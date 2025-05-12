package com.example.elderlycare2.data.remote.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: String
)