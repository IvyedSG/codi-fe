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
import org.codi.data.api.models.ProfileUpdateRequest
import org.codi.data.storage.TokenStorage
import org.codi.data.storage.profile.ProfileRepository

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(val profile: ProfileResponse) : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel {
    var state by mutableStateOf<ProfileState>(ProfileState.Loading)
        private set

    // Estados para el diálogo de edición
    var showEditDialog = mutableStateOf(false)
        private set

    var tempName = mutableStateOf("")
        private set

    var tempLastName = mutableStateOf("")
        private set

    var isUpdating = mutableStateOf(false)
        private set

    private val repository = ProfileRepository(ApiClient.router)

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
                    state = ProfileState.Error(response.error ?: response.message.takeIf { it.isNotBlank() } ?: "Error al cargar el perfil")
                }
            } catch (e: Exception) {
                state = ProfileState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Abre el diálogo de edición e inicializa los campos temporales
     * con los valores actuales del perfil
     */
    fun openEditDialog() {
        val currentState = state
        if (currentState is ProfileState.Success) {
            val userData = currentState.profile.data?.datosUsuario
            tempName.value = userData?.nombre ?: ""
            tempLastName.value = userData?.apellido ?: ""
            showEditDialog.value = true
        }
    }

    /**
     * Cierra el diálogo de edición
     */
    fun closeEditDialog() {
        showEditDialog.value = false
        tempName.value = ""
        tempLastName.value = ""
    }

    /**
     * Actualiza el nombre temporal
     */
    fun updateTempName(name: String) {
        tempName.value = name
    }

    /**
     * Actualiza el apellido temporal
     */
    fun updateTempLastName(lastName: String) {
        tempLastName.value = lastName
    }

    /**
     * Actualiza el perfil del usuario
     */
    fun updateProfile() {
        if (tempName.value.isBlank() || tempLastName.value.isBlank()) {
            return
        }

        isUpdating.value = true
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val request = ProfileUpdateRequest(
                    nombre = tempName.value.trim(),
                    apellido = tempLastName.value.trim()
                )

                val result = repository.updateProfile(request)

                result.onSuccess { response ->
                    if (response.success) {
                        // Actualizar el estado local con los nuevos valores
                        val currentState = state
                        if (currentState is ProfileState.Success) {
                            val updatedUserData = currentState.profile.data?.datosUsuario?.copy(
                                nombre = tempName.value.trim(),
                                apellido = tempLastName.value.trim()
                            )

                            val updatedData = currentState.profile.data?.copy(
                                datosUsuario = updatedUserData ?: currentState.profile.data.datosUsuario
                            )

                            val updatedProfile = currentState.profile.copy(
                                data = updatedData
                            )

                            state = ProfileState.Success(updatedProfile)
                        }
                        closeEditDialog()
                    } else {
                        state = ProfileState.Error(response.error ?: "Error al actualizar el perfil")
                    }
                }.onFailure { exception ->
                    state = ProfileState.Error(exception.message ?: "Error al actualizar el perfil")
                }
            } catch (e: Exception) {
                state = ProfileState.Error(e.message ?: "Error desconocido")
            } finally {
                isUpdating.value = false
            }
        }
    }

    /**
     * Cierra sesión del usuario
     * Limpia el token y el userId del almacenamiento
     */
    fun logout() {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                TokenStorage.clear()
            } catch (_: Exception) {
                // Ignorar errores al limpiar
            }
        }
    }
}
