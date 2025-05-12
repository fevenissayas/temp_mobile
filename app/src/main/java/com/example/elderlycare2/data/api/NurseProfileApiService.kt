package com.example.elderlycare2.data.api

import com.example.elderlycare2.data.remote.request.NurseProfileRequest
import com.example.elderlycare2.data.remote.response.NurseProfileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT

interface NurseProfileApiService {
    @GET("/nurse/profile")
    suspend fun getNurseProfile(
        @Header("Authorization") token: String
    ): NurseProfileResponse

    @PUT("/nurse/profile")
    suspend fun updateNurseProfile(
        @Header("Authorization") token: String,
        @Body nurseProfile: NurseProfileRequest
    ): NurseProfileResponse
}