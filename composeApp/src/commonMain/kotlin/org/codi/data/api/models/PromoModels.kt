package org.codi.data.api.models

import kotlinx.serialization.Serializable

// ============= Modelos del API (Serializables) =============

@Serializable
data class PromosResponse(
    val success: Boolean,
    val message: String,
    val data: PromosData
)

@Serializable
data class PromosData(
    val puntosUsuario: Int,
    val promociones: List<Promocion>
)

@Serializable
data class Promocion(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipoPromocion: String,
    val boletasRequeridas: Int,
    val validezInicio: String,
    val validezFin: String,
    val activa: Boolean,
    val tienda: TiendaPromo
)

@Serializable
data class TiendaPromo(
    val nombre: String,
    val urlLogo: String
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

