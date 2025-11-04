package org.codi.data.api.models

import kotlinx.serialization.Serializable

/**
 * Modelos para la funcionalidad de Perfil y Estadísticas.
 *
 * NOTA: Reutilizamos 'UserDto' para los datos básicos del usuario,
 * y definimos los modelos de respuesta y estadísticas aquí.
 */

// 1. Modelo específico para los datos de usuario del perfil
@Serializable
data class ProfileUserData(
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = ""
)

// 2. Modelo para la sección 'estadisticas'
@Serializable
data class ProfileStatistics(
    val cantidadRecibos: Int = 0,
    val cantidadRecibosVerdes: Int = 0,
    val co2Total: Double = 0.0, // Usamos Double para CO2 ya que puede ser decimal
    val co2Promedio: Double = 0.0
)

// 3. Modelo para la sección 'data' de la respuesta del perfil
@Serializable
data class ProfileData(
    val datosUsuario: ProfileUserData = ProfileUserData(),
    val estadisticas: ProfileStatistics = ProfileStatistics()
)

// 3. Modelo de Respuesta Completa para GET /perfil/{userId}
@Serializable
data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val data: ProfileData? = null,
    val error: String? = null
)

// 4. Modelo de Request para PUT /perfil/{userId}
@Serializable
data class ProfileUpdateRequest(
    val nombre: String,
    val apellido: String
)

// 5. Modelo de Respuesta para PUT /perfil/{userId}
@Serializable
data class ProfileUpdateResponse(
    val success: Boolean,
    val message: String,
    val error: String? = null
)
