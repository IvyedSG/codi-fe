package org.codi.data.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Función expect para obtener el timestamp actual en milisegundos
 */
expect fun getCurrentTimeMillis(): Long

/**
 * Entrada de caché con datos y timestamp
 */
data class CacheEntry<T>(
    val data: T,
    val timestamp: Long = getCurrentTimeMillis()
) {
    /**
     * Verifica si la entrada ha expirado
     * @param maxAgeMs Edad máxima en milisegundos
     */
    fun isExpired(maxAgeMs: Long): Boolean {
        val currentTime = getCurrentTimeMillis()
        return (currentTime - timestamp) > maxAgeMs
    }
}

/**
 * Manager genérico de caché en memoria con soporte para expiración
 * y actualización en segundo plano
 */
class CacheManager<T> {
    private var cacheEntry: CacheEntry<T>? = null
    private val mutex = Mutex()

    /**
     * Obtiene los datos del caché
     */
    suspend fun get(): T? = mutex.withLock {
        cacheEntry?.data
    }

    /**
     * Obtiene los datos del caché solo si no han expirado
     * @param maxAgeMs Edad máxima en milisegundos
     */
    suspend fun getIfValid(maxAgeMs: Long): T? = mutex.withLock {
        cacheEntry?.takeIf { !it.isExpired(maxAgeMs) }?.data
    }

    /**
     * Guarda datos en el caché
     */
    suspend fun set(data: T) = mutex.withLock {
        cacheEntry = CacheEntry(data)
    }

    /**
     * Limpia el caché
     */
    suspend fun clear() = mutex.withLock {
        cacheEntry = null
    }

    /**
     * Verifica si hay datos en caché
     */
    suspend fun hasData(): Boolean = mutex.withLock {
        cacheEntry != null
    }

    /**
     * Verifica si los datos en caché son válidos
     * @param maxAgeMs Edad máxima en milisegundos
     */
    suspend fun isValid(maxAgeMs: Long): Boolean = mutex.withLock {
        cacheEntry?.let { !it.isExpired(maxAgeMs) } ?: false
    }
}

/**
 * Constantes de tiempo de expiración de caché
 */
object CacheDuration {
    const val ONE_MINUTE = 60_000L
    const val TWO_MINUTES = 120_000L
    const val FIVE_MINUTES = 300_000L
    const val TEN_MINUTES = 600_000L
    const val THIRTY_MINUTES = 1_800_000L
    const val ONE_HOUR = 3_600_000L
}

