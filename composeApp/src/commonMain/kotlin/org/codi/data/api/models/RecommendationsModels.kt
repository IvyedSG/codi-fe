package org.codi.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class RecommendationProductOriginal(
    val id: String,
    val nombre: String,
    val co2: Double
)

@Serializable
data class RecommendationProductRecommended(
    val nombre: String,
    val marca: String? = null,
    val categoria: String? = null,
    val tienda: String? = null,
    val logoTienda: String? = null,
    val precio: Double? = null,
    val co2: Double
)

@Serializable
data class RecommendationImprovement(
    val porcentaje: Double,
    val co2Ahorrado: Double
)

@Serializable
data class RecommendationItem(
    val id: String,
    val productoOriginal: RecommendationProductOriginal,
    val productoRecomendado: RecommendationProductRecommended,
    val mejora: RecommendationImprovement,
    val tipo: String,
    val scoreSimilitud: Double
)

@Serializable
data class RecommendationsSummary(
    val totalRecomendaciones: Int,
    val co2TotalAhorrable: Double,
    val porcentajeMejoraPromedio: Double
)

@Serializable
data class RecommendationsData(
    val boletaId: String,
    val recomendaciones: List<RecommendationItem>,
    val resumen: RecommendationsSummary,
    val generadoEn: String
)

@Serializable
data class RecommendationsResponse(
    val success: Boolean,
    val message: String,
    val data: RecommendationsData? = null
)
