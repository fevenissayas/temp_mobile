package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.LoginApiService
import com.example.elderlycare2.data.api.NurseApiService
import com.example.elderlycare2.data.remote.response.NurseListResponse
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.flatMap

@Singleton
class NurseRepository @Inject constructor(private val nurseApiService: NurseApiService) {
    suspend fun getUserList(token: String): List<NurseListResponse> {
        return try {
            nurseApiService.getUserList("Bearer $token").users
        } catch (e: Exception) {
            throw Exception("Failed to fetch user list: ${e.message}")
        }
    }
}