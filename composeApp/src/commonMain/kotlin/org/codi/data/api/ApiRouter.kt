package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.*
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
        } catch (_: Throwable) {
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

    // Nueva función para registro usando DNI (dni,email,password)
    suspend fun registerByDni(registerRequest: org.codi.data.api.models.RegisterByDniRequest): RegisterResponse {
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

    // Nueva función para Google Sign-In
    suspend fun googleSignIn(googleRequest: GoogleSignInRequest): LoginResponse {
        return request<LoginResponse>(
            method = HttpMethod.Post,
            path = "/auth/google",
            body = googleRequest
        )
    }
    /**
     * Obtiene el perfil del usuario, incluyendo datos básicos y estadísticas.
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getUserProfile(userId: String): ProfileResponse {
        // La función genérica 'request' se encargará de obtener y deserializar el JSON.
        return request<ProfileResponse>(
            method = HttpMethod.Get,
            path = "/perfil/{userId}",
            routeParams = mapOf("userId" to userId) // Inserta el ID en la ruta
        )
    }

    /**
     * Actualiza el perfil del usuario (nombre y apellido).
     * @param userId El ID del usuario (UUID).
     * @param updateRequest Los nuevos datos del perfil.
     */
    suspend fun updateUserProfile(userId: String, updateRequest: ProfileUpdateRequest): ProfileUpdateResponse {
        return request<ProfileUpdateResponse>(
            method = HttpMethod.Put,
            path = "/perfil/{userId}",
            routeParams = mapOf("userId" to userId),
            body = updateRequest
        )
    }

    /**
     * Obtiene el historial de compras del usuario.
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getUserHistory(userId: String): HistoryResponse {
        return request<HistoryResponse>(
            method = HttpMethod.Get,
            path = "/historial/{userId}",
            routeParams = mapOf("userId" to userId)
        )
    }

    /**
     * Obtiene los datos de inicio del usuario.
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getHomeData(userId: String): HomeResponse {
        return request<HomeResponse>(
            method = HttpMethod.Get,
            path = "/inicio/{userId}",
            routeParams = mapOf("userId" to userId)
        )
    }

    /**
     * Obtiene todas las promociones disponibles.
     */
    suspend fun getPromociones(): PromosResponse {
        return request<PromosResponse>(
            method = HttpMethod.Get,
            path = "/promociones"
        )
    }

    /**
     * Obtiene las promociones de un usuario específico (promociones canjeadas).
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getPromocionesUsuario(userId: String): PromosResponse {
        return request<PromosResponse>(
            method = HttpMethod.Get,
            path = "/promociones",
            queryParams = mapOf("userId" to userId)
        )
    }

    /**
     * Obtiene el detalle de una promoción específica para un usuario.
     * @param promocionId El ID de la promoción (UUID).
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getPromocionDetalle(promocionId: String, userId: String): PromocionDetalleResponse {
        return request<PromocionDetalleResponse>(
            method = HttpMethod.Get,
            path = "/promociones/{promocionId}/usuario/{userId}",
            routeParams = mapOf(
                "promocionId" to promocionId,
                "userId" to userId
            )
        )
    }

    /**
     * Canjea una promoción.
     * @param request Los datos del canje (userId, promocionId, descripcion).
     */
    suspend fun canjearPromocion(request: CanjearPromoRequest): CanjearPromoResponse {
        return request<CanjearPromoResponse>(
            method = HttpMethod.Post,
            path = "/promociones/canjear",
            body = request
        )
    }

    /**
     * Obtiene el detalle de una boleta procesada.
     * @param boletaId El ID de la boleta (UUID).
     */
    suspend fun getBoletaDetalle(boletaId: String): BoletaDetalleResponse {
        return request<BoletaDetalleResponse>(
            method = HttpMethod.Get,
            path = "/boletas/{boletaId}",
            routeParams = mapOf("boletaId" to boletaId)
        )
    }

    /**
     * Sube y procesa una boleta mediante OCR.
     * @param userId El ID del usuario (UUID).
     * @param imageBytes Los bytes de la imagen de la boleta.
     * @param fileName El nombre del archivo (opcional).
     */
    suspend fun uploadBoleta(
        userId: String,
        imageBytes: ByteArray,
        fileName: String = "boleta.jpg"
    ): BoletaUploadResponse {
        val url = "$baseUrl/boletas/{userId}/upload".replace("{userId}", userId)

        val httpResponse: HttpResponse = client.post(url) {
            // Timeout extendido para procesamiento de imágenes con OCR
            timeout {
                requestTimeoutMillis = 60000  // 60 segundos para upload y OCR
                socketTimeoutMillis = 60000
            }

            setBody(
                io.ktor.client.request.forms.MultiPartFormDataContent(
                    io.ktor.client.request.forms.formData {
                        append("boleta", imageBytes, Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                        })
                    }
                )
            )
        }

        val status = httpResponse.status
        val responseText = try {
            httpResponse.bodyAsText()
        } catch (_: Throwable) {
            null
        }

        if (!status.isSuccess()) {
            val normalizedMessage = responseText?.let { txt ->
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

            val truncated = if (normalizedMessage.length > 1200) normalizedMessage.take(1200) + "..." else normalizedMessage

            throw ApiException(status, truncated)
        }

        return httpResponse.body()
    }

    /**
     * Obtiene datos de usuario por DNI (simulación)
     * @param dni DNI (8 dígitos)
     */
    suspend fun getUserByDni(dni: String): DniResponse {
        return request<DniResponse>(
            method = HttpMethod.Get,
            path = "/auth/user/dni/{dni}",
            routeParams = mapOf("dni" to dni)
        )
    }

    /**
     * Obtiene recomendaciones para una boleta (genera o retorna de caché en backend).
     * @param boletaId UUID de la boleta
     */
    suspend fun getRecommendations(boletaId: String): org.codi.data.api.models.RecommendationsResponse {
        return request<org.codi.data.api.models.RecommendationsResponse>(
            method = HttpMethod.Get,
            path = "/boletas/{boletaId}/recommendations",
            routeParams = mapOf("boletaId" to boletaId)
        )
    }

}
