package org.codi.data.api.models

import kotlinx.serialization.Serializable

/**
 * Modelos para la funcionalidad de Historial de Compras.
 */

// 1. Modelo para el resumen de actividad
@Serializable
data class ActivitySummary(
    val cantidadBoletas: Int = 0,
    val cantidadBoletasVerdes: Int = 0,
    val cantidadBoletasAmarillas: Int = 0,
    val cantidadBoletasRojas: Int = 0,
    val co2Total: Double = 0.0,
    val co2Promedio: Double = 0.0
)

// 2. Modelo para una compra reciente
@Serializable
data class RecentPurchase(
    val fechaBoleta: String = "", // Formato ISO 8601
    val logoTienda: String? = null, // Puede ser null
    val nombreTienda: String = "",
    val tipoBoleta: String = "VERDE", // "VERDE", "AMARILLA", "ROJA"
    val co2Boleta: Double = 0.0,
    val cantidadProductos: Int = 0
)

// 3. Modelo para la sección 'data' de la respuesta del historial
@Serializable
data class HistoryData(
    val resumenActividad: ActivitySummary? = null,
    val comprasRecientes: List<RecentPurchase> = emptyList()
)

// 4. Modelo de Respuesta Completa para GET /historial/{userId}
@Serializable
data class HistoryResponse(
    val success: Boolean,
    val message: String,
    val data: HistoryData? = null,
    val error: String? = null
)

