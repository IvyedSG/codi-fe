package org.codi.features.promos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.codi.data.api.ApiClient
import org.codi.data.api.models.Promocion
import org.codi.data.api.models.PromosResponse
import org.codi.data.storage.TokenStorage
import org.codi.data.storage.promo.PromoRepository
import org.codi.data.storage.home.HomeRepository
import org.codi.data.cache.CacheManager
import org.codi.data.cache.CacheDuration

data class PromoState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val puntosVerdes: Int = 0, // Puntos verdes desde /inicio/{userId}
    val promocionesDisponibles: List<Promocion> = emptyList(),
    val promocionesCanjeadas: List<Promocion> = emptyList(),
    val isRefreshing: Boolean = false
)

class PromoViewModel {
    private val promoRepository = PromoRepository(ApiClient.router)
    private val homeRepository = HomeRepository(ApiClient.router)

    var state by mutableStateOf(PromoState())
        private set

    // Estado del tab actual: 0 = Disponibles, 1 = Mis Canjes
    var currentTab by mutableIntStateOf(0)
        private set

    // Cachés separados para cada tipo de datos
    private val cacheDisponibles = CacheManager<PromosResponse>()
    private val cacheCanjeadas = CacheManager<PromosResponse>()
    private val cachePuntos = CacheManager<Int>()

    init {
        loadPuntosVerdes() // Cargar puntos verdes al inicio
        loadPromociones()
    }

    /**
     * Carga los puntos verdes desde el endpoint /inicio/{userId}
     */
    private fun loadPuntosVerdes(forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    return@launch
                }

                // Usar datos en caché si están disponibles
                val cachedPuntos = cachePuntos.getIfValid(CacheDuration.ONE_MINUTE)
                if (cachedPuntos != null && !forceRefresh) {
                    state = state.copy(puntosVerdes = cachedPuntos)
                    return@launch
                }

                homeRepository.getHomeDataByUserId(userId)
                    .onSuccess { response ->
                        if (response.success && response.data != null) {
                            cachePuntos.set(response.data.puntosVerdes)
                            state = state.copy(
                                puntosVerdes = response.data.puntosVerdes
                            )
                        }
                    }
                    .onFailure {
                        // Si hay datos en caché, usarlos aunque haya error
                        val fallbackPuntos = cachePuntos.get()
                        if (fallbackPuntos != null) {
                            state = state.copy(puntosVerdes = fallbackPuntos)
                        }
                    }
            } catch (_: Exception) {
                // Si hay datos en caché, usarlos aunque haya error
                val fallbackPuntos = cachePuntos.get()
                if (fallbackPuntos != null) {
                    state = state.copy(puntosVerdes = fallbackPuntos)
                }
            }
        }
    }

    /**
     * Cambia la pestaña seleccionada
     */
    fun selectTab(tabIndex: Int) {
        currentTab = tabIndex
        // Cargar datos según la pestaña seleccionada si no están en caché
        if (tabIndex == 0) {
            loadPromocionesDisponibles()
        } else {
            loadPromocionesCanjeadas()
        }
    }

    /**
     * Carga todas las promociones (disponibles inicialmente)
     */
    fun loadPromociones(forceRefresh: Boolean = false) {
        loadPromocionesDisponibles(forceRefresh)
    }

    /**
     * Carga las promociones disponibles (sin userId)
     */
    private fun loadPromocionesDisponibles(forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            // Si hay datos en caché válidos, usarlos
            val cachedData = cacheDisponibles.getIfValid(CacheDuration.FIVE_MINUTES)
            if (cachedData != null && !forceRefresh) {
                state = state.copy(
                    isLoading = false,
                    isRefreshing = false,
                    promocionesDisponibles = cachedData.data?.promociones ?: emptyList()
                )
                return@launch
            }

            // Mostrar loading solo si no hay datos en caché
            if (state.promocionesDisponibles.isEmpty()) {
                state = state.copy(isLoading = true, error = null)
            } else {
                state = state.copy(isRefreshing = true, error = null)
            }

            promoRepository.getPromociones()
                .onSuccess { response ->
                    if (response.success && response.data != null) {
                        cacheDisponibles.set(response)
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            promocionesDisponibles = response.data.promociones
                        )
                    } else {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = response.error ?: response.message.takeIf { it.isNotBlank() } ?: "No se pudieron cargar las promociones",
                            promocionesDisponibles = emptyList()
                        )
                    }
                }
                .onFailure { error ->
                    // Si hay datos en caché, usarlos aunque haya error
                    val fallbackData = cacheDisponibles.get()
                    if (fallbackData != null && fallbackData.data != null) {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            promocionesDisponibles = fallbackData.data.promociones
                        )
                    } else {
                        state = state.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = error.message ?: "Error al cargar promociones",
                            promocionesDisponibles = emptyList()
                        )
                    }
                }
        }
    }

    /**
     * Carga las promociones canjeadas del usuario (con userId)
     */
    private fun loadPromocionesCanjeadas(forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = "Usuario no autenticado",
                        promocionesCanjeadas = emptyList()
                    )
                    return@launch
                }

                // Si hay datos en caché válidos, usarlos
                val cachedData = cacheCanjeadas.getIfValid(CacheDuration.TWO_MINUTES)
                if (cachedData != null && !forceRefresh) {
                    state = state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        promocionesCanjeadas = cachedData.data?.promociones ?: emptyList()
                    )
                    return@launch
                }

                // Mostrar loading solo si no hay datos en caché
                if (state.promocionesCanjeadas.isEmpty()) {
                    state = state.copy(isLoading = true, error = null)
                } else {
                    state = state.copy(isRefreshing = true, error = null)
                }

                promoRepository.getPromocionesUsuario(userId)
                    .onSuccess { response ->
                        if (response.success && response.data != null) {
                            cacheCanjeadas.set(response)
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                promocionesCanjeadas = response.data.promociones
                            )
                        } else {
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = response.error ?: response.message.takeIf { it.isNotBlank() } ?: "No se pudieron cargar tus canjes",
                                promocionesCanjeadas = emptyList()
                            )
                        }
                    }
                    .onFailure { error ->
                        // Si hay datos en caché, usarlos aunque haya error
                        val fallbackData = cacheCanjeadas.get()
                        if (fallbackData != null && fallbackData.data != null) {
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                promocionesCanjeadas = fallbackData.data.promociones
                            )
                        } else {
                            state = state.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = error.message ?: "Error al cargar tus canjes",
                                promocionesCanjeadas = emptyList()
                            )
                        }
                    }
            } catch (e: Exception) {
                val fallbackData = cacheCanjeadas.get()
                if (fallbackData != null && fallbackData.data != null) {
                    state = state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        promocionesCanjeadas = fallbackData.data.promociones
                    )
                } else {
                    state = state.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = e.message ?: "Error desconocido",
                        promocionesCanjeadas = emptyList()
                    )
                }
            }
        }
    }

    /**
     * Canjea una promoción
     */
    fun canjearPromocion(promocionId: String) {
        state = state.copy(isLoading = true, error = null)

        CoroutineScope(Dispatchers.Default).launch {
            promoRepository.canjearPromocion(promocionId)
                .onSuccess { response ->
                    if (response.success) {
                        state = state.copy(
                            isLoading = false,
                            error = null
                        )
                        // Limpiar cachés para forzar recarga de datos actualizados
                        cacheDisponibles.clear()
                        cacheCanjeadas.clear()
                        cachePuntos.clear()

                        // Recargar promociones disponibles y puntos verdes
                        loadPromocionesDisponibles(forceRefresh = true)
                        loadPuntosVerdes(forceRefresh = true)
                    } else {
                        state = state.copy(
                            isLoading = false,
                            error = response.message
                        )
                    }
                }
                .onFailure { error ->
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Error al canjear promoción"
                    )
                }
        }
    }

    fun clearError() {
        state = state.copy(error = null)
    }

    /**
     * Limpia todos los cachés
     */
    fun clearCache() {
        CoroutineScope(Dispatchers.Default).launch {
            cacheDisponibles.clear()
            cacheCanjeadas.clear()
            cachePuntos.clear()
        }
    }
}

