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
import org.codi.data.api.models.RecommendationsResponse
import org.codi.data.storage.TokenStorage
import org.codi.data.cache.CacheManager
import org.codi.data.cache.CacheDuration

sealed class DetailState {
    object Idle : DetailState()
    object Loading : DetailState()
    data class Success(val response: BoletaDetalleResponse) : DetailState()
    data class Error(val message: String) : DetailState()
}

sealed class RecommendationsState {
    object Idle : RecommendationsState()
    data class Loading(val receiptId: String) : RecommendationsState()
    data class Success(val receiptId: String, val response: RecommendationsResponse) : RecommendationsState()
    data class Error(val message: String) : RecommendationsState()
}

sealed class UploadState {
    object Idle : UploadState()
    object Uploading : UploadState()
    data class Success(val response: BoletaUploadResponse) : UploadState()
    data class Error(val message: String) : UploadState()
}

class ReceiptViewModel {
    // Estado separado para detalle y recomendaciones para evitar que una petición sobrescriba a la otra
    var detailState by mutableStateOf<DetailState>(DetailState.Idle)
        private set

    var recommendationsState by mutableStateOf<RecommendationsState>(RecommendationsState.Idle)
        private set

    var uploadState by mutableStateOf<UploadState>(UploadState.Idle)
        private set

    private val cache = CacheManager<BoletaDetalleResponse>()
    private val recommendationsCache = CacheManager<RecommendationsResponse>()

    /**
     * Sube una imagen de boleta para procesamiento.
     */
    fun uploadReceipt(imageBytes: ByteArray, fileName: String = "boleta.jpg") {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                uploadState = UploadState.Uploading

                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    uploadState = UploadState.Error("Usuario no autenticado")
                    return@launch
                }

                val response = ApiClient.router.uploadBoleta(
                    userId = userId,
                    imageBytes = imageBytes,
                    fileName = fileName
                )

                if (response.success) {
                    uploadState = UploadState.Success(response)
                } else {
                    uploadState = UploadState.Error(response.message)
                }
            } catch (e: Exception) {
                uploadState = UploadState.Error(e.message ?: "Error al procesar la boleta")
            }
        }
    }

    /**
     * Carga el detalle de una boleta procesada.
     */
    fun loadReceiptDetail(boletaId: String, forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val cacheKey = "receipt_$boletaId"

                val cachedData = cache.getIfValid(CacheDuration.FIVE_MINUTES, cacheKey)
                if (cachedData != null && !forceRefresh) {
                    detailState = DetailState.Success(cachedData)
                    return@launch
                }

                detailState = DetailState.Loading

                val response = ApiClient.router.getBoletaDetalle(boletaId)

                if (response.success) {
                    cache.set(response, cacheKey)
                    detailState = DetailState.Success(response)
                } else {
                    detailState = DetailState.Error(response.message)
                }
            } catch (e: Exception) {
                detailState = DetailState.Error(e.message ?: "Error al cargar el detalle de la boleta")
            }
        }
    }

    /**
     * Obtiene (o genera) recomendaciones para una boleta.
     * Usa caché local para evitar peticiones repetidas en la misma sesión.
     */
    fun loadRecommendations(boletaId: String, forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val cacheKey = "reco_$boletaId"
                val cached = recommendationsCache.getIfValid(CacheDuration.FIVE_MINUTES, cacheKey)
                if (cached != null && !forceRefresh) {
                    recommendationsState = RecommendationsState.Success(boletaId, cached)
                    return@launch
                }

                recommendationsState = RecommendationsState.Loading(boletaId)

                val resp = ApiClient.router.getRecommendations(boletaId)

                if (resp.success && resp.data != null) {
                    recommendationsCache.set(resp, cacheKey)
                    recommendationsState = RecommendationsState.Success(boletaId, resp)
                } else {
                    recommendationsState = RecommendationsState.Error(resp.message)
                }
            } catch (e: Exception) {
                recommendationsState = RecommendationsState.Error(e.message ?: "Error al obtener recomendaciones")
            }
        }
    }

    fun resetStates() {
        detailState = DetailState.Idle
        recommendationsState = RecommendationsState.Idle
        uploadState = UploadState.Idle
    }
}
