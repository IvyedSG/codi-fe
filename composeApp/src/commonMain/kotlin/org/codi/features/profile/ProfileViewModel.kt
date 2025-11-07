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
import org.codi.data.cache.CacheManager
import org.codi.data.cache.CacheDuration

sealed class ProfileState {
    object Loading : ProfileState()
    data class Success(
        val profile: ProfileResponse,
        val isRefreshing: Boolean = false
    ) : ProfileState()
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
    private val cache = CacheManager<ProfileResponse>()

    /**
     * Carga el perfil del usuario.
     * Si hay datos en caché válidos, los muestra inmediatamente
     * y actualiza en segundo plano.
     * @param forceRefresh Fuerza la recarga ignorando el caché
     */
    fun loadProfile(forceRefresh: Boolean = false) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    state = ProfileState.Error("Usuario no autenticado")
                    return@launch
                }

                // Si hay datos en caché válidos y no es un refresh forzado
                val cachedData = cache.getIfValid(CacheDuration.FIVE_MINUTES)
                if (cachedData != null && !forceRefresh) {
                    // Mostrar datos en caché inmediatamente
                    state = ProfileState.Success(
                        profile = cachedData,
                        isRefreshing = false
                    )

                    // Actualizar en segundo plano si los datos tienen más de 1 minuto
                    if (!cache.isValid(CacheDuration.ONE_MINUTE)) {
                        refreshInBackground(userId)
                    }
                    return@launch
                }

                // Si no hay caché o es refresh forzado, mostrar loading
                if (state !is ProfileState.Success) {
                    state = ProfileState.Loading
                } else {
                    // Si ya hay datos mostrados, solo indicar que está refrescando
                    val currentState = state as ProfileState.Success
                    state = currentState.copy(isRefreshing = true)
                }

                // Cargar datos frescos
                val response = ApiClient.router.getUserProfile(userId)

                if (response.success && response.data != null) {
                    // Guardar en caché
                    cache.set(response)

                    state = ProfileState.Success(
                        profile = response,
                        isRefreshing = false
                    )
                } else {
                    state = ProfileState.Error(response.error ?: response.message.takeIf { it.isNotBlank() } ?: "Error al cargar el perfil")
                }
            } catch (e: Exception) {
                // Si hay datos en caché, mostrarlos aunque haya error
                val cachedData = cache.get()
                if (cachedData != null) {
                    state = ProfileState.Success(
                        profile = cachedData,
                        isRefreshing = false
                    )
                } else {
                    state = ProfileState.Error(e.message ?: "Error desconocido")
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
                val response = ApiClient.router.getUserProfile(userId)
                if (response.success && response.data != null) {
                    cache.set(response)
                    state = ProfileState.Success(
                        profile = response,
                        isRefreshing = false
                    )
                }
            } catch (e: Exception) {
                // Silenciosamente ignorar errores en refresh de fondo
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
                            // Actualizar caché con nuevos datos
                            cache.set(updatedProfile)
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
                cache.clear()
            } catch (_: Exception) {
                // Ignorar errores al limpiar
            }
        }
    }

    /**
     * Limpia el caché
     */
    fun clearCache() {
        CoroutineScope(Dispatchers.Default).launch {
            cache.clear()
        }
    }
}
