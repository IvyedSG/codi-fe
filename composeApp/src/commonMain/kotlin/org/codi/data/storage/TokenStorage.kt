package org.codi.data.storage

// TokenStorage.kt
expect object TokenStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun getRefreshToken(): String?
    suspend fun saveUserId(userId: String)
    suspend fun getUserId(): String?
    suspend fun clear()
}
