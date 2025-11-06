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
import org.codi.data.storage.TokenStorage
import org.codi.data.storage.promo.PromoRepository
import org.codi.data.storage.home.HomeRepository

data class PromoState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val puntosVerdes: Int = 0, // Puntos verdes desde /inicio/{userId}
    val promocionesDisponibles: List<Promocion> = emptyList(),
    val promocionesCanjeadas: List<Promocion> = emptyList()
)

class PromoViewModel {
    private val promoRepository = PromoRepository(ApiClient.router)
    private val homeRepository = HomeRepository(ApiClient.router)

    var state by mutableStateOf(PromoState())
        private set

    // Estado del tab actual: 0 = Disponibles, 1 = Mis Canjes
    var currentTab by mutableIntStateOf(0)
        private set

    init {
        loadPuntosVerdes() // Cargar puntos verdes al inicio
        loadPromociones()
    }

    /**
     * Carga los puntos verdes desde el endpoint /inicio/{userId}
     */
    private fun loadPuntosVerdes() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    return@launch
                }

                homeRepository.getHomeDataByUserId(userId)
                    .onSuccess { response ->
                        if (response.success && response.data != null) {
                            state = state.copy(
                                puntosVerdes = response.data.puntosVerdes
                            )
                        }
                    }
                    .onFailure {
                        // No mostrar error, simplemente mantener puntos en 0
                    }
            } catch (_: Exception) {
                // No mostrar error, simplemente mantener puntos en 0
            }
        }
    }

    /**
     * Cambia la pestaña seleccionada
     */
    fun selectTab(tabIndex: Int) {
        currentTab = tabIndex
        // Recargar datos según la pestaña seleccionada
        if (tabIndex == 0) {
            loadPromocionesDisponibles()
        } else {
            loadPromocionesCanjeadas()
        }
    }

    /**
     * Carga todas las promociones (disponibles inicialmente)
     */
    fun loadPromociones() {
        loadPromocionesDisponibles()
    }

    /**
     * Carga las promociones disponibles (sin userId)
     */
    private fun loadPromocionesDisponibles() {
        state = state.copy(isLoading = true, error = null)

        CoroutineScope(Dispatchers.Default).launch {
            promoRepository.getPromociones()
                .onSuccess { response ->
                    if (response.success && response.data != null) {
                        state = state.copy(
                            isLoading = false,
                            promocionesDisponibles = response.data.promociones
                        )
                    } else {
                        state = state.copy(
                            isLoading = false,
                            error = response.error ?: response.message.takeIf { it.isNotBlank() } ?: "No se pudieron cargar las promociones",
                            promocionesDisponibles = emptyList()
                        )
                    }
                }
                .onFailure { error ->
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar promociones",
                        promocionesDisponibles = emptyList()
                    )
                }
        }
    }

    /**
     * Carga las promociones canjeadas del usuario (con userId)
     */
    private fun loadPromocionesCanjeadas() {
        state = state.copy(isLoading = true, error = null)

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = state.copy(
                        isLoading = false,
                        error = "Usuario no autenticado",
                        promocionesCanjeadas = emptyList()
                    )
                    return@launch
                }

                promoRepository.getPromocionesUsuario(userId)
                    .onSuccess { response ->
                        if (response.success && response.data != null) {
                            state = state.copy(
                                isLoading = false,
                                promocionesCanjeadas = response.data.promociones
                            )
                        } else {
                            state = state.copy(
                                isLoading = false,
                                error = response.error ?: response.message.takeIf { it.isNotBlank() } ?: "No se pudieron cargar tus canjes",
                                promocionesCanjeadas = emptyList()
                            )
                        }
                    }
                    .onFailure { error ->
                        state = state.copy(
                            isLoading = false,
                            error = error.message ?: "Error al cargar tus canjes",
                            promocionesCanjeadas = emptyList()
                        )
                    }
            } catch (e: Exception) {
                state = state.copy(
                    isLoading = false,
                    error = e.message ?: "Error desconocido",
                    promocionesCanjeadas = emptyList()
                )
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
                        // Recargar promociones disponibles y puntos verdes
                        loadPromocionesDisponibles()
                        loadPuntosVerdes()
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
}

