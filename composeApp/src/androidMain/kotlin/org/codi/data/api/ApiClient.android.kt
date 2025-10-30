package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.engine.android.*

/**
 * Implementación actual para Android.
 * Usa el motor Android de Ktor.
 */
actual fun createHttpClient(): HttpClient {
    return HttpClient(Android)
}

