package org.codi.data.storage

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.codi.AppContextHolder

private const val PREFS_NAME = "codi_prefs"
private const val KEY_TOKEN = "auth_token"

actual object TokenStorage {
    private val prefs: SharedPreferences by lazy {
        AppContextHolder.appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    actual suspend fun saveToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    actual suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_TOKEN, null)
    }

    actual suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}

