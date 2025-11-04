package org.codi.features.promos

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.codi.data.api.ApiClient
import org.codi.data.api.models.Promocion
import org.codi.data.storage.promo.PromoRepository

data class PromoState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val puntosUsuario: Int = 0,
    val promociones: List<Promocion> = emptyList()
)

class PromoViewModel {
    private val repository = PromoRepository(ApiClient.router)

    var state by mutableStateOf(PromoState())
        private set

    init {
        loadPromociones()
    }

    fun loadPromociones() {
        state = state.copy(isLoading = true, error = null)

        CoroutineScope(Dispatchers.Default).launch {
            repository.getPromociones()
                .onSuccess { response ->
                    state = state.copy(
                        isLoading = false,
                        puntosUsuario = response.data.puntosUsuario,
                        promociones = response.data.promociones
                    )
                }
                .onFailure { error ->
                    state = state.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar promociones"
                    )
                }
        }
    }

    fun canjearPromocion(promocionId: String, descripcion: String = "") {
        state = state.copy(isLoading = true, error = null)

        CoroutineScope(Dispatchers.Default).launch {
            repository.canjearPromocion(promocionId, descripcion)
                .onSuccess { response ->
                    if (response.success) {
                        state = state.copy(
                            isLoading = false,
                            error = null
                        )
                        // Recargar promociones para reflejar cambios
                        loadPromociones()
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

