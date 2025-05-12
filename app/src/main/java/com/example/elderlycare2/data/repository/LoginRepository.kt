package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.LoginApiService
import com.example.elderlycare2.data.remote.request.LoginRequest
import com.example.elderlycare2.data.remote.response.LoginResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val loginApiService: LoginApiService) {
    suspend fun loginUser(email: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(email, password)
        val response = loginApiService.loginUser(loginRequest)
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null && body.token.isNotEmpty()) {
                return body
            } else {
                throw Exception("Invalid login response: empty token")
            }
        } else {
            throw Exception("Login failed: ${response.code()}")
        }
    }
}