package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.CareScheduleApiService
import com.example.elderlycare2.data.remote.request.AddScheduleRequest
import com.example.elderlycare2.data.remote.response.AddScheduleResponse
import com.example.elderlycare2.data.remote.response.NurseListResponse
import javax.inject.Inject

class CareScheduleRepository @Inject constructor(private val careScheduleApiService: CareScheduleApiService) {
    suspend fun addSchedule(
        token: String,
        addScheduleRequest: AddScheduleRequest
    ): AddScheduleResponse {
        return try {
            careScheduleApiService.addSchedule("Bearer $token", addScheduleRequest)
        } catch (e: Exception) {
            throw Exception("Error adding schedule: ${e.message}")
        }
    }
    suspend fun fetchUserList(token: String): List<NurseListResponse> {
        return try {
            careScheduleApiService.fetchUserList("Bearer $token").users
        } catch (e: Exception) {
            throw Exception("Failed to fetch user list: ${e.message}")
        }
    }
}