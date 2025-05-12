package com.example.elderlycare2.data.repository

import com.example.elderlycare2.data.api.UserProfileApi
import com.example.elderlycare2.data.remote.request.UserEditProfileRequest
import com.example.elderlycare2.data.remote.response.UserProfileResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileRepository @Inject constructor(
    private val userProfileApi: UserProfileApi
) {
    suspend fun getUserProfile(token: String): UserProfileResponse {
        return try {
            userProfileApi.getUserProfile("Bearer $token")
        } catch (e: Exception) {
            throw Exception("Failed to fetch user profile: ${e.message}")
        }
    }

    suspend fun updateUserProfile(
        token: String,
        userEditProfileRequest: UserEditProfileRequest
    ): UserProfileResponse {
        return try {
            userProfileApi.updateUserProfile(userEditProfileRequest,"Bearer $token")
        } catch (e: Exception) {
            throw Exception("Failed to update user profile: ${e.message}")
        }
    }
}