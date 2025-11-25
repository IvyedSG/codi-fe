package org.codi.data.api.models

import kotlinx.serialization.Serializable

/**
 * Modelos para la simulación de búsqueda por DNI
 */

@Serializable
data class DniUser(
    val nombre: String = "",
    val apellido: String = "",
    val dni: String = ""
)

@Serializable
data class DniResponse(
    val success: Boolean,
    val message: String,
    val data: DniUser? = null,
    val error: String? = null
)

