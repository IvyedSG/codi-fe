package org.codi

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import org.codi.theme.CodiTheme
import org.codi.ui.ToastHost
import cafe.adriel.voyager.navigator.Navigator
import org.codi.features.splash.SplashScreen

@Composable
fun App() {
    CodiTheme {
        // Configurar los colores de las barras del sistema
        SetSystemBarsColor(
            statusBarColor = Color(0xFFE8F0F0), // Mismo color que el fondo
            navigationBarColor = Color.White, // Mismo color que el NavigationBar
            isDarkIcons = true // Iconos oscuros porque el fondo es claro
        )

        // Iniciar en SplashScreen para verificar si hay sesión activa
        Navigator(SplashScreen)

        // Toast global
        ToastHost()
    }
}
