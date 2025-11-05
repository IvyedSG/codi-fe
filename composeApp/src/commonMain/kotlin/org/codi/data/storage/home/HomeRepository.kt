package org.codi.data.storage.home

import org.codi.data.api.ApiRouter
import org.codi.data.api.models.HomeResponse
import org.codi.data.storage.TokenStorage

class HomeRepository(
    private val apiRouter: ApiRouter
) {

    /**
     * Obtiene los datos de inicio del usuario autenticado
     */
    suspend fun getHomeData(): Result<HomeResponse> = runCatching {
        val userId = TokenStorage.getUserId()
            ?: throw IllegalStateException("Usuario no autenticado")

        apiRouter.getHomeData(userId)
    }

    /**
     * Obtiene los datos de inicio de un usuario específico
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getHomeDataByUserId(userId: String): Result<HomeResponse> = runCatching {
        apiRouter.getHomeData(userId)
    }
}

