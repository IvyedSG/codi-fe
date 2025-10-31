package org.codi.data.storage

import platform.Foundation.NSUserDefaults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val KEY_TOKEN = "auth_token"

actual object TokenStorage {
    private val defaults = NSUserDefaults.standardUserDefaults()

    actual suspend fun saveToken(token: String) = withContext(Dispatchers.Default) {
        defaults.setObject(token, forKey = KEY_TOKEN)
    }

    actual suspend fun getToken(): String? = withContext(Dispatchers.Default) {
        val value = defaults.stringForKey(KEY_TOKEN)
        value
    }

    actual suspend fun clear() = withContext(Dispatchers.Default) {
        defaults.removeObjectForKey(KEY_TOKEN)
    }
}

