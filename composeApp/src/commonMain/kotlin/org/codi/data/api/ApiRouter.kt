package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.codi.data.api.models.*

class ApiException(val status: HttpStatusCode, val body: String?) : RuntimeException("API error ${status.value}: ${body}")

/**
 * Un router/fachada para invocar endpoints de forma dinámica.
 * path: string como "/user/login" o "/receipts/{id}"
 * params: mapa de parámetros de ruta, query o cuerpo
 */
class ApiRouter(
    @PublishedApi internal val client: HttpClient,
    @PublishedApi internal val baseUrl: String
) {

    suspend inline fun <reified T> request(
        method: HttpMethod = HttpMethod.Get,
        path: String,
        routeParams: Map<String, String> = emptyMap(),
        queryParams: Map<String, String> = emptyMap(),
        body: Any? = null
    ): T {
        // Reemplaza parámetros de ruta en `path`
        val resolvedPath = routeParams.entries.fold(path) { acc, (key, value) ->
            acc.replace("{$key}", value)
        }

        val url = "$baseUrl$resolvedPath"

        val httpResponse: HttpResponse = client.request(url) {
            this.method = method
            // Query params
            if (queryParams.isNotEmpty()) {
                url {
                    queryParams.forEach { (k, v) -> parameters.append(k, v) }
                }
            }
            // Body si aplica
            if (body != null) {
                this.setBody(body)
                contentType(ContentType.Application.Json)
            }
        }

        val status = httpResponse.status
        val responseText = try {
            httpResponse.bodyAsText()
        } catch (t: Throwable) {
            null
        }

        if (!status.isSuccess()) {
            // Manejo fino por códigos comunes
            val normalizedMessage = responseText?.let { txt ->
                // Intentar parsear JSON y extraer 'message' o 'error'
                try {
                    val json = Json.parseToJsonElement(txt)
                    val obj = json.jsonObject
                    val maybeMsg = try { obj["message"]?.jsonPrimitive?.content } catch (_: Throwable) { null }
                        ?: try { obj["error"]?.jsonPrimitive?.content } catch (_: Throwable) { null }
                    maybeMsg?.trim()?.takeIf { it.isNotEmpty() } ?: txt.trim()
                } catch (_: Throwable) {
                    txt.trim()
                }
            }?.takeIf { it.isNotBlank() } ?: "HTTP ${status.value} ${status.description}"

            // Truncar para evitar mensajes excesivamente largos (por ejemplo stacktraces)
            val truncated = if (normalizedMessage.length > 1200) normalizedMessage.take(1200) + "..." else normalizedMessage

            when (status) {
                HttpStatusCode.BadRequest -> throw ApiException(status, truncated)
                HttpStatusCode.Conflict -> throw ApiException(status, truncated)
                else -> throw ApiException(status, truncated)
            }
        }

        return httpResponse.body()
    }

    // Nueva función específica para register
    suspend fun register(registerRequest: RegisterRequest): RegisterResponse {
        return request<RegisterResponse>(
            method = HttpMethod.Post,
            path = "/auth/register",
            body = registerRequest
        )
    }

    // Nueva función específica para login
    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        return request<LoginResponse>(
            method = HttpMethod.Post,
            path = "/auth/login",
            body = loginRequest
        )
    }
}
