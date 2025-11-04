// commonMain/kotlin/org.codi.data.storage.profile/ProfileRepository.kt

package org.codi.data.storage.profile

import org.codi.data.api.ApiRouter
import org.codi.data.api.models.ProfileResponse
import org.codi.data.api.models.ProfileUpdateRequest
import org.codi.data.api.models.ProfileUpdateResponse
import org.codi.data.storage.TokenStorage

// Nota: Necesitarás una manera de obtener el ID del usuario actual.
// Asumiremos que el token de almacenamiento también guarda (o permite derivar) el ID.

class ProfileRepository(
    private val apiRouter: ApiRouter,
    // Deberías inyectar o tener acceso a una clase que dé el ID del usuario
    // private val tokenStorage: TokenStorage // Si TokenStorage guarda el ID
) {

    // Función que llama al router
    suspend fun getProfile(userId: String): Result<ProfileResponse> = runCatching {
        // En un caso real, obtendrías el ID del usuario autenticado aquí,
        // pero por ahora, lo pasamos como parámetro.
        apiRouter.getUserProfile(userId)
    }

    /**
     * Actualiza el perfil del usuario.
     * Obtiene el userId desde TokenStorage y llama al endpoint PUT /perfil/{userId}
     */
    suspend fun updateProfile(request: ProfileUpdateRequest): Result<ProfileUpdateResponse> = runCatching {
        val userId = TokenStorage.getUserId()
            ?: throw IllegalStateException("Usuario no autenticado")

        apiRouter.updateUserProfile(userId, request)
    }
}