package org.codi.data.storage

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.codi.AppContextHolder

private const val PREFS_NAME = "codi_prefs"
private const val KEY_TOKEN = "auth_token"
private const val KEY_USER_ID = "user_id"
private const val KEY_REFRESH_TOKEN = "refresh_token"

actual object TokenStorage {
    private val prefs: SharedPreferences by lazy {
        AppContextHolder.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    actual suspend fun saveToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    actual suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_TOKEN, null)
    }

    actual suspend fun saveRefreshToken(refreshToken: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply()
    }

    actual suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    actual suspend fun saveUserId(userId: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    actual suspend fun getUserId(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_USER_ID, null)
    }

    actual suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_REFRESH_TOKEN).remove(KEY_USER_ID).apply()
    }
}
