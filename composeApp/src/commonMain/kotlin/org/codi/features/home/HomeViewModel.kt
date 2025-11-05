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

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val home: HomeResponse) : HomeState()
    data class Error(val message: String) : HomeState()
}

class HomeViewModel {
    var state by mutableStateOf<HomeState>(HomeState.Loading)
        private set

    private val repository = HomeRepository(ApiClient.router)

    fun loadHomeData() {
        state = HomeState.Loading
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = HomeState.Error("Usuario no autenticado")
                    return@launch
                }

                val response = ApiClient.router.getHomeData(userId)

                if (response.success && response.data != null) {
                    state = HomeState.Success(response)
                } else {
                    state = HomeState.Error(response.error ?: response.message.takeIf { it.isNotBlank() } ?: "Error al cargar los datos de inicio")
                }
            } catch (e: Exception) {
                state = HomeState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

