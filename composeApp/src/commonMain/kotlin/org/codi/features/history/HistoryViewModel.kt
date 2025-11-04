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

sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(val history: HistoryResponse) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

class HistoryViewModel {
    var state by mutableStateOf<HistoryState>(HistoryState.Loading)
        private set

    private val repository = HistoryRepository(ApiClient.router)

    fun loadHistory() {
        state = HistoryState.Loading
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = HistoryState.Error("Usuario no autenticado")
                    return@launch
                }

                val response = ApiClient.router.getUserHistory(userId)

                if (response.success && response.data != null) {
                    state = HistoryState.Success(response)
                } else {
                    state = HistoryState.Error(response.error ?: "Error al cargar el historial")
                }
            } catch (e: Exception) {
                state = HistoryState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

