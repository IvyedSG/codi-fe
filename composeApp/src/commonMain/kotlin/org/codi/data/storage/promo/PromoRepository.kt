package org.codi.data.storage.promo

import org.codi.data.api.ApiRouter
import org.codi.data.api.models.CanjearPromoRequest
import org.codi.data.api.models.CanjearPromoResponse
import org.codi.data.api.models.PromocionDetalleResponse
import org.codi.data.api.models.PromosResponse
import org.codi.data.storage.TokenStorage

class PromoRepository(
    private val apiRouter: ApiRouter
) {
    /**
     * Obtiene todas las promociones disponibles (sin userId).
     */
    suspend fun getPromociones(): Result<PromosResponse> = runCatching {
        apiRouter.getPromociones()
    }

    /**
     * Obtiene las promociones canjeadas de un usuario específico.
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getPromocionesUsuario(userId: String): Result<PromosResponse> = runCatching {
        apiRouter.getPromocionesUsuario(userId)
    }

    /**
     * Obtiene el detalle de una promoción específica para un usuario.
     * @param promocionId El ID de la promoción (UUID).
     * @param userId El ID del usuario (UUID).
     */
    suspend fun getPromocionDetalle(promocionId: String, userId: String): Result<PromocionDetalleResponse> = runCatching {
        apiRouter.getPromocionDetalle(promocionId, userId)
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

