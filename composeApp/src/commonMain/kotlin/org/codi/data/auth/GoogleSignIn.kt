package org.codi.data.auth

import androidx.compose.runtime.Composable

/**
 * Resultado de la autenticación de Google
 */
data class GoogleSignInResult(
    val success: Boolean,
    val idToken: String? = null,
    val error: String? = null
)

/**
 * Función Composable multiplataforma para iniciar sesión con Google
 * Debe ser llamada desde un @Composable para configurar el launcher correctamente
 */
@Composable
expect fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInResult) -> Unit
): () -> Unit

