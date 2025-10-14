package org.codi

import androidx.compose.runtime.Composable
import org.codi.features.auth.LoginScreen
import org.codi.theme.CodiTheme

@Composable
fun App() {
    CodiTheme {
        LoginScreen(
            onLoginSuccess = {
            }
        )
    }
}
