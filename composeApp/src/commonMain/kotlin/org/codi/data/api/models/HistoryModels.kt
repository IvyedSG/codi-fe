package org.codi.data.api.models

import kotlinx.serialization.Serializable

/**
 * Modelos para la funcionalidad de Historial de Compras.
 */

// 1. Modelo para el resumen de actividad
@Serializable
data class ActivitySummary(
    val cantidadBoletas: Int,
    val cantidadBoletasVerdes: Int,
    val cantidadBoletasAmarillas: Int,
    val cantidadBoletasRojas: Int,
    val co2Total: Double,
    val co2Promedio: Double
)

// 2. Modelo para una compra reciente
@Serializable
data class RecentPurchase(
    val fechaBoleta: String, // Formato ISO 8601
    val logoTienda: String,
    val nombreTienda: String,
    val tipoBoleta: String, // "VERDE", "AMARILLA", "ROJA"
    val co2Boleta: Double,
    val cantidadProductos: Int
)

// 3. Modelo para la sección 'data' de la respuesta del historial
@Serializable
data class HistoryData(
    val resumenActividad: ActivitySummary,
    val comprasRecientes: List<RecentPurchase>
)

// 4. Modelo de Respuesta Completa para GET /historial/{userId}
@Serializable
data class HistoryResponse(
    val success: Boolean,
    val message: String,
    val data: HistoryData? = null,
    val error: String? = null
)

