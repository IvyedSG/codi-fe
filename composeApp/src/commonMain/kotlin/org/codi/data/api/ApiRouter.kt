package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

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

        val httpResponse = client.request(url) {
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

        // Verificar éxito y parsear
        if (!httpResponse.status.isSuccess()) {
            throw RuntimeException("HTTP error ${httpResponse.status}")
        }

        return httpResponse.body()
    }
}
