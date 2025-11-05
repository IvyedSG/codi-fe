package org.codi.data.api.models

import kotlinx.serialization.Serializable

// ============= Modelos del API (Serializables) =============

@Serializable
data class PromosResponse(
    val success: Boolean,
    val message: String = "",
    val data: PromosData? = null,
    val error: String? = null
)

@Serializable
data class PromosData(
    val puntosUsuario: Int = 0,
    val promociones: List<Promocion> = emptyList()
)

@Serializable
data class Promocion(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String? = null,
    val tipoPromocion: String = "",
    val boletasRequeridas: Int = 0,
    val validezInicio: String? = null,
    val validezFin: String? = null,
    val activa: Boolean = true,
    val tienda: TiendaPromo? = null,
    // Campos para promociones canjeadas
    val disponible: Boolean? = null,
    val fechaUso: String? = null,
    val detalles: String? = null
)

@Serializable
data class TiendaPromo(
    val nombre: String = "",
    val urlLogo: String? = null
)

@Serializable
data class CanjearPromoRequest(
    val userId: String,
    val promocionId: String,
    val descripcion: String
)

@Serializable
data class CanjearPromoResponse(
    val success: Boolean,
    val message: String,
    val data: CanjearPromoData? = null
)

@Serializable
data class CanjearPromoData(
    val canjeId: String? = null,
    val mensaje: String? = null
)

// Modelo para el detalle de promoción (GET /promociones/{promocionId}/usuario/{userId})
@Serializable
data class PromocionDetalleResponse(
    val success: Boolean,
    val message: String = "",
    val data: Promocion? = null,
    val error: String? = null
)
