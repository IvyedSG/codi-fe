package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.HttpTimeout
import kotlinx.serialization.json.Json
import org.codi.data.storage.TokenStorage
import io.ktor.serialization.kotlinx.json.json

/**
 * Función expect que será implementada en cada plataforma
 * para crear el HttpClient con el motor apropiado
 */
expect fun createHttpClient(): HttpClient

/**
 * Objeto singleton para configurar y proporcionar el cliente API.
 * Usa Ktor con serialización JSON.
 */
object ApiClient {
    private val httpClient = createHttpClient().config {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 30000
        }
    }
    /**
     * Router API para hacer llamadas a endpoints
     */
    val router = ApiRouter(httpClient, BASE_API_URL)

    suspend fun <T> withAuthRetry(block: suspend () -> T): T {
        try {
            return block()
        } catch (e: ApiException) {
            if (e.status.value == 401) {
                val refreshToken = TokenStorage.getRefreshToken()
                if (refreshToken != null) {
                    try {
                        val refreshResponse = router.refreshToken(refreshToken)
                        TokenStorage.saveToken(refreshResponse.token)
                        TokenStorage.saveRefreshToken(refreshResponse.refreshToken)
                        // Reintentar la petición original
                        return block()
                    } catch (refreshError: Exception) {
                        TokenStorage.clear()
                        throw refreshError
                    }
                } else {
                    TokenStorage.clear()
                }
            }
            throw e
        }
    }
}

/**
 * Ejemplo de uso:
 *
 * // Definir data class con @Serializable
 * @Serializable
 * data class User(val id: Int, val name: String)
 *
 * @Serializable
 * data class LoginRequest(val email: String, val password: String)
 *
 * // Hacer una petición GET
 * val user: User = ApiClient.router.request(
 *     method = HttpMethod.Get,
 *     path = "/users/{id}",
 *     routeParams = mapOf("id" to "123")
 * )
 *
 * // Hacer una petición POST
 * val response: User = ApiClient.router.request(
 *     method = HttpMethod.Post,
 *     path = "/auth/login",
 *     body = LoginRequest(email = "user@example.com", password = "pass123")
 * )
 *
 * // Hacer una petición GET con query params
 * val users: List<User> = ApiClient.router.request(
 *     method = HttpMethod.Get,
 *     path = "/users",
 *     queryParams = mapOf("page" to "1", "limit" to "10")
 * )
 */
