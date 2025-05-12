package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.VisitDetailsApiService
import com.example.elderlycare2.data.remote.request.ViewDetailRequest
import com.example.elderlycare2.data.remote.response.ViewDetailResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewDetailRepository @Inject constructor(private val visitDetailsApiService: VisitDetailsApiService) {
    suspend fun getVisitDetails(token: String, elderId: String): ViewDetailResponse {
        return try {
            visitDetailsApiService.getVisitDetails("Bearer $token", elderId)
        } catch (e: Exception) {
            throw Exception("Get visit details failed: ${e.message}")
        }
    }
    suspend fun updateVisitDetails(
        token: String,
        elderId: String,
        viewDetails: ViewDetailRequest
    ): ViewDetailResponse {
        return try {
            visitDetailsApiService.updateVisitDetails("Bearer $token", elderId, viewDetails)
        } catch (e: Exception) {
            throw Exception("Update visit details failed: ${e.message}")
        }
    }
}