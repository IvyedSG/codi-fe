package org.codi.data.storage.history

import org.codi.data.api.ApiRouter
import org.codi.data.api.models.HistoryResponse
import org.codi.data.storage.TokenStorage

class HistoryRepository(
    private val apiRouter: ApiRouter
) {

    /**
     * Obtiene el historial de compras del usuario autenticado
     */
    suspend fun getHistory(): Result<HistoryResponse> = runCatching {
        val userId = TokenStorage.getUserId()
            ?: throw IllegalStateException("Usuario no autenticado")

        apiRouter.getUserHistory(userId)
    }
}

