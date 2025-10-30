package org.codi.data.api

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

/**
 * Implementación actual para iOS.
 * Usa el motor Darwin (iOS/macOS) de Ktor.
 */
actual fun createHttpClient(): HttpClient {
    return HttpClient(Darwin)
}

