package org.codi.data.storage

expect object TokenStorage {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clear()
}

