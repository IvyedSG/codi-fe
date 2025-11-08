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
    private val cacheMap = mutableMapOf<String, CacheEntry<T>>()
    private val mutex = Mutex()

    /**
     * Obtiene los datos del caché
     */
    suspend fun get(key: String = "default"): T? = mutex.withLock {
        if (key == "default") {
            cacheEntry?.data
        } else {
            cacheMap[key]?.data
        }
    }

    /**
     * Obtiene los datos del caché solo si no han expirado
     * @param maxAgeMs Edad máxima en milisegundos
     * @param key Clave del caché (opcional)
     */
    suspend fun getIfValid(maxAgeMs: Long, key: String = "default"): T? = mutex.withLock {
        if (key == "default") {
            cacheEntry?.takeIf { !it.isExpired(maxAgeMs) }?.data
        } else {
            cacheMap[key]?.takeIf { !it.isExpired(maxAgeMs) }?.data
        }
    }

    /**
     * Guarda datos en el caché
     * @param data Datos a guardar
     * @param key Clave del caché (opcional)
     */
    suspend fun set(data: T, key: String = "default") = mutex.withLock {
        val entry = CacheEntry(data)
        if (key == "default") {
            cacheEntry = entry
        } else {
            cacheMap[key] = entry
        }
    }

    /**
     * Limpia el caché
     * @param key Clave del caché a limpiar (opcional, si no se especifica limpia todo)
     */
    suspend fun clear(key: String? = null) = mutex.withLock {
        if (key == null) {
            cacheEntry = null
            cacheMap.clear()
        } else if (key == "default") {
            cacheEntry = null
        } else {
            cacheMap.remove(key)
        }
    }

    /**
     * Verifica si hay datos en caché
     * @param key Clave del caché (opcional)
     */
    suspend fun hasData(key: String = "default"): Boolean = mutex.withLock {
        if (key == "default") {
            cacheEntry != null
        } else {
            cacheMap.containsKey(key)
        }
    }

    /**
     * Verifica si los datos en caché son válidos
     * @param maxAgeMs Edad máxima en milisegundos
     * @param key Clave del caché (opcional)
     */
    suspend fun isValid(maxAgeMs: Long, key: String = "default"): Boolean = mutex.withLock {
        if (key == "default") {
            cacheEntry?.let { !it.isExpired(maxAgeMs) } ?: false
        } else {
            cacheMap[key]?.let { !it.isExpired(maxAgeMs) } ?: false
        }
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

