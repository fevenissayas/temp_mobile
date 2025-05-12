package com.example.elderlycare2.data.api

import com.example.elderlycare2.data.remote.request.SignUpRequest
import com.example.elderlycare2.data.remote.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApiService {
    @POST("auth/user/signup")
    suspend fun signUpUser(
        @Body signUpRequest: SignUpRequest
    ): Response<SignUpResponse>

    @POST("nurse/signup")
    suspend fun signUpNurse(
        @Body signUpRequest: SignUpRequest
    ): Response<SignUpResponse>
}