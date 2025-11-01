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
data class GoogleSignInRequest(
    val idToken: String
)

@Serializable
data class UserDto(
    val id: String,
    val nombre: String,
    val apellido: String,
    val email: String,
    val proveedorAuth: String? = null
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

typealias LoginResponse = RegisterResponse

