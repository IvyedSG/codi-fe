/*
package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.MockRequestHandlerScope
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import org.codi.data.api.models.*

class ApiRouterTest {

    private fun makeClient(responseStatus: HttpStatusCode, responseBody: String): HttpClient {
        val engine = MockEngine { request ->
            respond(responseBody, responseStatus, headers = headersOf("Content-Type" to listOf("application/json")))
        }

        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Test
    fun `register success returns data`() = runBlocking {
        val body = """
        {
          "success": true,
          "message": "Usuario registrado exitosamente",
          "data": {
            "user": { "id": "1", "nombre": "Juan", "apellido": "Perez", "email": "juan@example.com" },
            "token": "token-abc",
            "refreshToken": "refresh-abc"
          }
        }
        """.trimIndent()

        val client = makeClient(HttpStatusCode.OK, body)
        val router = ApiRouter(client, "http://test")

        val resp = router.register(RegisterRequest("Juan", "Perez", "juan@example.com", "pass"))
        assertEquals(true, resp.success)
        assertEquals("Usuario registrado exitosamente", resp.message)
        assertEquals("token-abc", resp.data?.token)
    }

    @Test
    fun `register bad request throws ApiException`() = runBlocking {
        val body = "{ \"success\": false, \"message\": \"Datos invalidos\", \"error\": \"invalid\" }"
        val client = makeClient(HttpStatusCode.BadRequest, body)
        val router = ApiRouter(client, "http://test")

        val ex = assertFailsWith<ApiException> {
            runBlocking {
                router.register(RegisterRequest("", "", "", ""))
            }
        }
        assertEquals(HttpStatusCode.BadRequest, ex.status)
        assertEquals(true, ex.body?.contains("Datos invalidos") ?: false)
    }

    @Test
    fun `register conflict throws ApiException`() = runBlocking {
        val body = "{ \"success\": false, \"message\": \"Email ya registrado\", \"error\": \"conflict\" }"
        val client = makeClient(HttpStatusCode.Conflict, body)
        val router = ApiRouter(client, "http://test")

        val ex = assertFailsWith<ApiException> {
            runBlocking {
                router.register(RegisterRequest("Juan", "Perez", "juan@example.com", "pass"))
            }
        }
        assertEquals(HttpStatusCode.Conflict, ex.status)
        assertEquals(true, ex.body?.contains("Email ya registrado") ?: false)
    }
}
*/
