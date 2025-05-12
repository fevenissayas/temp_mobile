package com.example.elderlycare2.data.api

import com.example.elderlycare2.data.remote.request.ViewDetailRequest
import com.example.elderlycare2.data.remote.response.ViewDetailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface VisitDetailsApiService {
    @GET("/nurse/user/{id}/details")
    suspend fun getVisitDetails(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): ViewDetailResponse

    @PUT("/nurse/user/{id}/details")
    suspend fun updateVisitDetails(
        @Header("Authorization") token: String,
        @Path("id") userId: String,
        @Body viewDetails: ViewDetailRequest
    ): ViewDetailResponse
}