// ProfileViewModel.kt
package org.codi.features.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.codi.data.api.ApiClient
import org.codi.data.api.models.ProfileResponse
import org.codi.data.storage.TokenStorage

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: ProfileResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel {
    var state by mutableStateOf<ProfileState>(ProfileState.Loading)
        private set

    fun loadProfile() {
        state = ProfileState.Loading
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = ProfileState.Error("Usuario no autenticado")
                    return@launch
                }

                val response = ApiClient.router.getUserProfile(userId)

                if (response.success && response.data != null) {
                    state = ProfileState.Success(response)
                } else {
                    state = ProfileState.Error(response.error ?: "Error al cargar el perfil")
                }
            } catch (e: Exception) {
                state = ProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
