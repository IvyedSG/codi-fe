package org.codi.features.receipt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.codi.data.api.ApiClient
import org.codi.data.api.models.BoletaDetalleResponse
import org.codi.data.api.models.BoletaUploadResponse
import org.codi.data.storage.TokenStorage
import org.codi.data.cache.CacheManager
import org.codi.data.cache.CacheDuration

sealed class ReceiptState {
    object Idle : ReceiptState()
    object Uploading : ReceiptState()
    object Loading : ReceiptState()
    data class UploadSuccess(val response: BoletaUploadResponse) : ReceiptState()
    data class DetailSuccess(val response: BoletaDetalleResponse) : ReceiptState()
    data class Error(val message: String) : ReceiptState()
}

class ReceiptViewModel {
    var state by mutableStateOf<ReceiptState>(ReceiptState.Idle)
        private set

    private val cache = CacheManager<BoletaDetalleResponse>()

    /**
     * Sube una imagen de boleta para procesamiento.
     * @param imageBytes Los bytes de la imagen.
     * @param fileName El nombre del archivo (opcional).
     */
    fun uploadReceipt(imageBytes: ByteArray, fileName: String = "boleta.jpg") {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                state = ReceiptState.Uploading

                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = ReceiptState.Error("Usuario no autenticado")
                    return@launch
                }

                val response = ApiClient.router.uploadBoleta(
                    userId = userId,
                    imageBytes = imageBytes,
                    fileName = fileName
                )

                if (response.success) {
                    state = ReceiptState.UploadSuccess(response)
                } else {
                    state = ReceiptState.Error(response.message)
                }
            } catch (e: Exception) {
                state = ReceiptState.Error(
                    e.message ?: "Error al procesar la boleta"
                )
            }
        }
    }

    /**
     * Carga el detalle de una boleta procesada.
     * @param boletaId El ID de la boleta.
     * @param forceRefresh Fuerza la recarga ignorando el caché.
     */
    fun loadReceiptDetail(boletaId: String, forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val cacheKey = "receipt_$boletaId"

                // Si hay datos en caché válidos y no es un refresh forzado
                val cachedData = cache.getIfValid(CacheDuration.FIVE_MINUTES, cacheKey)
                if (cachedData != null && !forceRefresh) {
                    state = ReceiptState.DetailSuccess(cachedData)
                    return@launch
                }

                state = ReceiptState.Loading

                val response = ApiClient.router.getBoletaDetalle(boletaId)

                if (response.success) {
                    // Guardar en caché
                    cache.set(response, cacheKey)
                    state = ReceiptState.DetailSuccess(response)
                } else {
                    state = ReceiptState.Error(response.message)
                }
            } catch (e: Exception) {
                state = ReceiptState.Error(
                    e.message ?: "Error al cargar el detalle de la boleta"
                )
            }
        }
    }

    /**
     * Resetea el estado a Idle.
     */
    fun resetState() {
        state = ReceiptState.Idle
    }
}

