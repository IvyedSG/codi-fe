package org.codi.data.storage.promo

import org.codi.data.api.ApiRouter
import org.codi.data.api.models.CanjearPromoRequest
import org.codi.data.api.models.CanjearPromoResponse
import org.codi.data.api.models.PromosResponse
import org.codi.data.storage.TokenStorage

class PromoRepository(
    private val apiRouter: ApiRouter
) {
    /**
     * Obtiene todas las promociones disponibles.
     */
    suspend fun getPromociones(): Result<PromosResponse> = runCatching {
        apiRouter.getPromociones()
    }

    /**
     * Canjea una promoción.
     * @param promocionId El ID de la promoción a canjear.
     * @param descripcion Descripción o mensaje del canje.
     */
    suspend fun canjearPromocion(promocionId: String, descripcion: String): Result<CanjearPromoResponse> = runCatching {
        val userId = TokenStorage.getUserId()
            ?: throw IllegalStateException("Usuario no autenticado")

        val request = CanjearPromoRequest(
            userId = userId,
            promocionId = promocionId,
            descripcion = descripcion
        )

        apiRouter.canjearPromocion(request)
    }
}

