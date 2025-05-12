package com.example.elderlycare2.data.api

import com.example.elderlycare2.data.remote.response.NurseDeleteResponse
import com.example.elderlycare2.data.remote.response.NurseListResponse
import com.example.elderlycare2.data.remote.response.NurseListResponseWrapper
import com.example.elderlycare2.data.remote.response.ViewDetailResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface NurseApiService {
    @GET("/nurse")
    suspend fun getUserList(@Header("Authorization") token: String): NurseListResponseWrapper

    @GET("/nurse/user/{id}/details")
    suspend fun viewDetails(): List<ViewDetailResponse>

    @DELETE("/nurse/user/{id}/details/delete")
    suspend fun nurseDeleteUser(
        @Header("Authorization") token: String,
        @Path("id") userId: String
    ): NurseDeleteResponse

}