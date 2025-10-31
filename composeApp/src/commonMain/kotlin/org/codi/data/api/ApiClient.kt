package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

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

    /**
     * Cliente HTTP configurado con negociación de contenido JSON
     * Usa el motor específico de cada plataforma y agrega logging/timeout.
     */
    private val httpClient = createHttpClient().config {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.INFO
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 10000
            socketTimeoutMillis = 15000
        }
    }

    /**
     * Router API para hacer llamadas a endpoints
     */
    val router = ApiRouter(httpClient, BASE_API_URL)
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
