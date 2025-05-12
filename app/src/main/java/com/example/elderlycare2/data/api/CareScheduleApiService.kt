package com.example.elderlycare2.data.api

import com.example.elderlycare2.data.remote.request.AddScheduleRequest
import com.example.elderlycare2.data.remote.response.AddScheduleResponse
import com.example.elderlycare2.data.remote.response.NurseListResponseWrapper
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface CareScheduleApiService {
    @POST("/nurse/addTask")
    suspend fun addSchedule(
        @Header("Authorization") token: String,
        @Body schedule: AddScheduleRequest
    ): AddScheduleResponse

    @GET("/nurse")
    suspend fun fetchUserList(@Header("Authorization") token: String): NurseListResponseWrapper
}