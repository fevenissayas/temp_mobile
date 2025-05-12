package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.SignUpApiService
import com.example.elderlycare2.data.remote.request.SignUpRequest
import com.example.elderlycare2.data.remote.response.SignUpResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignUpRepository @Inject constructor(private val signUpApiService: SignUpApiService) {
    suspend fun signUpUser(email: String, password: String, name: String): SignUpResponse {
        val signUpRequest = SignUpRequest(email, password, name, "USER")
        val response = signUpApiService.signUpUser(signUpRequest)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Sign up failed: ${response.code()}")
        }
    }

    suspend fun signUpNurse(email: String, password: String, name: String): SignUpResponse {
        val signUpRequest = SignUpRequest(email, password, name, "NURSE")
        val response = signUpApiService.signUpNurse(signUpRequest)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Empty response body")
        } else {
            throw Exception("Sign up failed: ${response.code()}")
        }
    }
}