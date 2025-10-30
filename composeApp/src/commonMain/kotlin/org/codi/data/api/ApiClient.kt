package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
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

    private const val BASE_URL = "https://api.ejemplo.com" // Cambia esto a tu URL base

    /**
     * Cliente HTTP configurado con negociación de contenido JSON
     * Usa el motor específico de cada plataforma
     */
    private val httpClient = createHttpClient().config {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    /**
     * Router API para hacer llamadas a endpoints
     */
    val router = ApiRouter(httpClient, BASE_URL)
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

