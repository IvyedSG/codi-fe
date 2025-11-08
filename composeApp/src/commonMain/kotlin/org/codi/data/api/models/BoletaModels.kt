package org.codi.data.api.models

import kotlinx.serialization.Serializable

/**
 * Modelos para la funcionalidad de Boletas.
 */

// ===== Modelos para GET /boletas/{boletaId} =====

@Serializable
data class ProductoBoleta(
    val id: String,
    val nombre: String,
    val cantidad: Double,
    val precioUnitario: Double,
    val precioTotal: Double,
    val factorCo2: Double,
    val categoria: String? = null,  // Nullable porque el backend puede enviar null
    val subcategoria: String? = null,
    val marca: String? = null,
    val esLocal: Boolean? = null,
    val tieneEmpaqueEcologico: Boolean? = null
)

@Serializable
data class AnalisisBoleta(
    val totalProductos: Int,
    val co2Total: Double,
    val co2Promedio: Double,
    val productosVerdes: Int? = null,
    val porcentajeVerde: Double? = null,
    val tipoAmbiental: String? = null,
    val esReciboVerde: Boolean? = null
)

@Serializable
data class BoletaDetalle(
    val id: String,
    val fechaBoleta: String,
    val nombreTienda: String,
    val logoTienda: String? = null,
    val total: Double,
    val tipoAmbiental: String? = null,
    val urlImagen: String? = null,
    val productos: List<ProductoBoleta> = emptyList(),
    val analisis: AnalisisBoleta
)

@Serializable
data class BoletaDetalleResponse(
    val success: Boolean,
    val message: String,
    val data: BoletaDetalle? = null,
    val error: String? = null
)

// ===== Modelos para POST /boletas/{userId}/upload =====

@Serializable
data class ProductoUpload(
    val nombre: String,
    val precio: Double,
    val cantidad: Double,
    val categoria: String? = null,  // Nullable porque el backend puede enviar null
    val subcategoria: String? = null,
    val marcaId: String? = null,
    val factorCo2: Double,
    val esLocal: Boolean? = null,
    val tieneEmpaqueEcologico: Boolean? = null,
    val confianza: Double? = null
)

@Serializable
data class AnalisisUpload(
    val totalProductos: Int,
    val productosVerdes: Int,
    val porcentajeVerde: Double,
    val co2Total: Double,
    val co2Promedio: Double,
    val tipoAmbiental: String,
    val esReciboVerde: Boolean
)

@Serializable
data class BoletaUploadData(
    val boletaId: String,
    val analisis: AnalisisUpload,
    val productos: List<ProductoUpload> = emptyList(),
    val sugerencias: List<String> = emptyList()
)

@Serializable
data class BoletaUploadResponse(
    val success: Boolean,
    val message: String,
    val data: BoletaUploadData? = null,
    val error: String? = null
)

