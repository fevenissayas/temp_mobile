package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.NurseProfileApiService
import com.example.elderlycare2.data.remote.request.NurseProfileRequest
import com.example.elderlycare2.data.remote.response.NurseProfileResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NurseProfileRespository @Inject constructor(
    private val nurseProfileApiService: NurseProfileApiService
) {
    suspend fun getNurseProfile(token: String): NurseProfileResponse {
        return try {
            nurseProfileApiService.getNurseProfile("Bearer $token")
        } catch (e: Exception) {
            throw Exception("Failed to fetch nurse profile: ${e.message}")
        }
    }
    suspend fun updateNurseProfile(
        token: String,
        nurseProfile: NurseProfileRequest
    ): NurseProfileResponse {
        return try {
            nurseProfileApiService.updateNurseProfile("Bearer $token", nurseProfile)
        } catch (e: Exception) {
            throw Exception("Failed to update nurse profile: ${e.message}")
        }
    }
}