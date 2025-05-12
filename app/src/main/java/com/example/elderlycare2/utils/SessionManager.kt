package com.example.elderlycare2.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Base64
import org.json.JSONObject
import kotlin.apply

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "AppPrefs_MSA"
        private const val USER_TOKEN = "user_token"
        private const val ROLE = "role"
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun getUserInfoFromToken(): Map<String, Any>? {
        val token = fetchAuthToken() ?: return null
        val parts = token.split(".")
        if (parts.size != 3) return null
        val payload = parts[1]
        val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
        val json = String(decodedBytes, Charsets.UTF_8)
        val jsonObject = JSONObject(json)
        return jsonObject.keys().asSequence().associateWith { key -> jsonObject.get(key) }
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserRole(role: String) {
        prefs.edit().putString(ROLE, role).apply()
    }

    fun fetchRole(): String? {
        val role = prefs.getString(ROLE, null)
        return if (role == "nurse" || role == "user") role else null
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}