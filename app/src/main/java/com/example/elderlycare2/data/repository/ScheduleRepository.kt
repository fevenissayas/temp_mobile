package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.ScheduleApi
import com.example.elderlycare2.data.remote.response.TaskResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepository @Inject constructor(
    private val scheduleApi: ScheduleApi
) {
    suspend fun getTasks(token: String): List<TaskResponse> {
        return try {
            val response = scheduleApi.getTasks("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.tasks
            } else {
                throw Exception("Failed to fetch tasks: ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Get tasks failed: ${e.message}")
        }
    }
}