package org.codi.data.storage

actual object TokenStorage {
    private var token: String? = null

    actual suspend fun saveToken(tokenValue: String) {
        token = tokenValue
    }

    actual suspend fun getToken(): String? {
        return token
    }

    actual suspend fun clear() {
        token = null
    }
}

