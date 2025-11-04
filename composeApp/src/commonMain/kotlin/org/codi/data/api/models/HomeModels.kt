package org.codi.data.api.models

import kotlinx.serialization.Serializable

/**
 * Modelos para la funcionalidad de Inicio/Home.
 */

// 1. Modelo para la última boleta
@Serializable
data class LastReceipt(
    val nombreTienda: String = "",
    val categoriaTienda: String = "",
    val logoTienda: String? = null, // Puede ser null
    val co2Total: Double = 0.0,
    val fechaBoleta: String = "", // Formato ISO 8601
    val precioTotal: Double = 0.0
)

// 2. Modelo para una promoción
@Serializable
data class Promotion(
    val titulo: String = "",
    val tipoPromocion: String = ""
)

// 3. Modelo para la sección 'data' de la respuesta de inicio
@Serializable
data class HomeData(
    val puntosVerdes: Int = 0,
    val co2Acumulado: Double = 0.0,
    val ultimaBoleta: LastReceipt? = null,
    val promociones: List<Promotion> = emptyList()
)

// 4. Modelo de Respuesta Completa para GET /inicio/{userId}
@Serializable
data class HomeResponse(
    val success: Boolean,
    val message: String,
    val data: HomeData? = null,
    val error: String? = null
)

