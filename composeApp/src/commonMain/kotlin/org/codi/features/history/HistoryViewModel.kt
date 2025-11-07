package org.codi.features.history

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.codi.data.api.ApiClient
import org.codi.data.api.models.HistoryResponse
import org.codi.data.storage.TokenStorage
import org.codi.data.storage.history.HistoryRepository
import org.codi.data.cache.CacheManager
import org.codi.data.cache.CacheDuration

sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(
        val history: HistoryResponse,
        val isRefreshing: Boolean = false
    ) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

class HistoryViewModel {
    var state by mutableStateOf<HistoryState>(HistoryState.Loading)
        private set

    private val repository = HistoryRepository(ApiClient.router)
    private val cache = CacheManager<HistoryResponse>()

    /**
     * Carga el historial del usuario.
     * Si hay datos en caché válidos, los muestra inmediatamente
     * y actualiza en segundo plano.
     * @param forceRefresh Fuerza la recarga ignorando el caché
     */
    fun loadHistory(forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = HistoryState.Error("Usuario no autenticado")
                    return@launch
                }

                // Si hay datos en caché válidos y no es un refresh forzado
                val cachedData = cache.getIfValid(CacheDuration.TWO_MINUTES)
                if (cachedData != null && !forceRefresh) {
                    // Mostrar datos en caché inmediatamente
                    state = HistoryState.Success(
                        history = cachedData,
                        isRefreshing = false
                    )

                    // Actualizar en segundo plano si los datos tienen más de 30 segundos
                    if (!cache.isValid(30_000L)) {
                        refreshInBackground(userId)
                    }
                    return@launch
                }

                // Si no hay caché o es refresh forzado, mostrar loading
                if (state !is HistoryState.Success) {
                    state = HistoryState.Loading
                } else {
                    // Si ya hay datos mostrados, solo indicar que está refrescando
                    val currentState = state as HistoryState.Success
                    state = currentState.copy(isRefreshing = true)
                }

                // Cargar datos frescos
                val response = ApiClient.router.getUserHistory(userId)

                if (response.success && response.data != null) {
                    // Guardar en caché
                    cache.set(response)

                    state = HistoryState.Success(
                        history = response,
                        isRefreshing = false
                    )
                } else {
                    state = HistoryState.Error(response.error ?: response.message.takeIf { it.isNotBlank() } ?: "Error al cargar el historial")
                }
            } catch (e: Exception) {
                // Si hay datos en caché, mostrarlos aunque haya error
                val cachedData = cache.get()
                if (cachedData != null) {
                    state = HistoryState.Success(
                        history = cachedData,
                        isRefreshing = false
                    )
                } else {
                    state = HistoryState.Error(e.message ?: "Error desconocido")
                }
            }
        }
    }

    /**
     * Actualiza los datos en segundo plano sin mostrar loading
     */
    private fun refreshInBackground(userId: String) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val response = ApiClient.router.getUserHistory(userId)
                if (response.success && response.data != null) {
                    cache.set(response)
                    state = HistoryState.Success(
                        history = response,
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                // Silenciosamente ignorar errores en refresh de fondo
            }
        }
    }

    /**
     * Limpia el caché
     */
    fun clearCache() {
        CoroutineScope(Dispatchers.Default).launch {
            cache.clear()
        }
    }
}

