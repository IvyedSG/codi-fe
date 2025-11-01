package org.codi.data.auth

import androidx.compose.runtime.Composable

/**
 * Implementación stub de Google Sign-In para iOS
 * TODO: Implementar cuando se necesite soporte para iOS
 */
@Composable
actual fun rememberGoogleSignInLauncher(
    onResult: (GoogleSignInResult) -> Unit
): () -> Unit {
    return {
        onResult(
            GoogleSignInResult(
                success = false,
                error = "Google Sign-In no está implementado en iOS aún"
            )
        )
    }
}

