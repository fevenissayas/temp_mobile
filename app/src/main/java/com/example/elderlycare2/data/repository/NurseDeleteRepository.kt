package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.NurseApiService
import com.example.elderlycare2.data.remote.response.NurseDeleteResponse
import javax.inject.Inject

class NurseDeleteRepository @Inject constructor(
    private val nurseApiService: NurseApiService
) {
    suspend fun nurseDeleteUser(token: String, userId: String): NurseDeleteResponse {
        return nurseApiService.nurseDeleteUser("Bearer $token", userId)
    }
}