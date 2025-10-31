package org.codi.data.api.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val nombre: String,
    val apellido: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class UserDto(
    val id: String,
    val nombre: String,
    val apellido: String,
    val email: String
)

@Serializable
data class RegisterData(
    val user: UserDto,
    val token: String,
    val refreshToken: String
)

@Serializable
data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val data: RegisterData? = null,
    val error: String? = null
)

// Reusar el mismo esquema para login
typealias LoginResponse = RegisterResponse
