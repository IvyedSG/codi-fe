package org.codi.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.codi.data.api.ApiClient
import org.codi.data.api.models.HomeResponse
import org.codi.data.storage.TokenStorage
import org.codi.data.storage.home.HomeRepository
import org.codi.data.cache.CacheManager
import org.codi.data.cache.CacheDuration

sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val home: HomeResponse,
        val isEmpty: Boolean = false,
        val isRefreshing: Boolean = false
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel {
    var state by mutableStateOf<HomeState>(HomeState.Loading)
        private set

    // Estados para mensajes de feedback
    var successMessage = mutableStateOf<String?>(null)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    private val repository = HomeRepository(ApiClient.router)
    private val cache = CacheManager<HomeResponse>()

    /**
     * Carga los datos del home.
     * Si hay datos en caché válidos, los muestra inmediatamente
     * y actualiza en segundo plano.
     * @param forceRefresh Fuerza la recarga ignorando el caché
     */
    fun loadHomeData(forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = HomeState.Error("Usuario no autenticado")
                    errorMessage.value = "Sesión expirada. Por favor, inicia sesión nuevamente."
                    return@launch
                }

                // Si hay datos en caché válidos y no es un refresh forzado
                val cachedData = cache.getIfValid(CacheDuration.TWO_MINUTES)
                if (cachedData != null && !forceRefresh) {
                    // Mostrar datos en caché inmediatamente
                    val isEmpty = cachedData.data?.let {
                        it.puntosVerdes == 0 &&
                        it.ultimaBoleta == null &&
                        it.co2Acumulado == 0.0
                    } ?: false

                    state = HomeState.Success(
                        home = cachedData,
                        isEmpty = isEmpty,
                        isRefreshing = false
                    )

                    // Actualizar en segundo plano si los datos tienen más de 30 segundos
                    if (!cache.isValid(30_000L)) {
                        refreshInBackground(userId)
                    }
                    return@launch
                }

                // Si no hay caché o es refresh forzado, mostrar loading
                if (state !is HomeState.Success) {
                    state = HomeState.Loading
                } else {
                    // Si ya hay datos mostrados, solo indicar que está refrescando
                    val currentState = state as HomeState.Success
                    state = currentState.copy(isRefreshing = true)
                }

                // Cargar datos frescos
                val response = ApiClient.router.getHomeData(userId)

                if (response.success && response.data != null) {
                    // Guardar en caché
                    cache.set(response)

                    val isEmpty = response.data.puntosVerdes == 0 &&
                                  response.data.ultimaBoleta == null &&
                                  response.data.co2Acumulado == 0.0

                    state = HomeState.Success(
                        home = response,
                        isEmpty = isEmpty,
                        isRefreshing = false
                    )

                    // Mostrar mensaje de éxito solo si fue un refresh manual
                    if (forceRefresh) {
                        successMessage.value = "✓ Datos actualizados"
                    }
                } else {
                    val errorMsg = response.error ?: response.message.takeIf { it.isNotBlank() } ?: "Error al cargar los datos de inicio"
                    state = HomeState.Error(errorMsg)
                    errorMessage.value = errorMsg
                }
            } catch (e: Exception) {
                // Si hay datos en caché, mostrarlos aunque haya error
                val cachedData = cache.get()
                if (cachedData != null) {
                    val isEmpty = cachedData.data?.let {
                        it.puntosVerdes == 0 &&
                        it.ultimaBoleta == null &&
                        it.co2Acumulado == 0.0
                    } ?: false

                    state = HomeState.Success(
                        home = cachedData,
                        isEmpty = isEmpty,
                        isRefreshing = false
                    )
                    errorMessage.value = "No se pudo actualizar. Mostrando datos guardados."
                } else {
                    val errorMsg = when {
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "Error de conexión. Verifica tu internet."
                        e.message?.contains("timeout", ignoreCase = true) == true ->
                            "La operación tardó demasiado. Inténtalo de nuevo."
                        else -> e.message ?: "Error de conexión. Verifica tu conexión a internet."
                    }
                    state = HomeState.Error(errorMsg)
                    errorMessage.value = errorMsg
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
                val response = ApiClient.router.getHomeData(userId)
                if (response.success && response.data != null) {
                    cache.set(response)

                    val isEmpty = response.data.puntosVerdes == 0 &&
                                  response.data.ultimaBoleta == null &&
                                  response.data.co2Acumulado == 0.0

                    state = HomeState.Success(
                        home = response,
                        isEmpty = isEmpty,
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                // Silenciosamente ignorar errores en refresh de fondo
            }
        }
    }

    /**
     * Limpia los mensajes de éxito y error
     */
    fun clearMessages() {
        successMessage.value = null
        errorMessage.value = null
    }

    /**
     * Limpia el caché (útil al hacer logout)
     */
    fun clearCache() {
        CoroutineScope(Dispatchers.Default).launch {
            cache.clear()
        }
    }
}

