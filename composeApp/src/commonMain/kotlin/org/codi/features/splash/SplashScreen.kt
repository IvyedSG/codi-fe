package org.codi.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import codi.composeapp.generated.resources.Res
import codi.composeapp.generated.resources.logo
import kotlinx.coroutines.delay
import org.codi.data.storage.TokenStorage
import org.codi.features.auth.LoginScreen
import org.codi.ui.HomeTabNavigator
import org.codi.theme.PrimaryGreen
import org.jetbrains.compose.resources.painterResource

/**
 * SplashScreen: Pantalla de bienvenida que verifica si el usuario tiene una sesión activa.
 *
 * Flujo:
 * 1. Muestra logo y loading por 1.5 segundos
 * 2. Verifica si existe un token guardado
 * 3. Si hay token → HomeTabNavigator (sesión activa)
 * 4. Si no hay token → LoginScreen
 */
object SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            // Esperar un poco para mostrar el splash
            delay(1500)

            // Verificar si hay un token guardado
            val token = TokenStorage.getToken()
            val userId = TokenStorage.getUserId()

            if (!token.isNullOrEmpty() && !userId.isNullOrEmpty()) {
                // Hay sesión activa, ir al HomeTabNavigator
                navigator.replaceAll(HomeTabNavigator)
            } else {
                // No hay sesión, ir al Login
                navigator.replaceAll(LoginScreen)
            }
        }

        SplashContent()
    }
}

@Composable
private fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE8F0F0)), // Mismo color que el fondo de la app
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Image(
                painter = painterResource(Res.drawable.logo),
                contentDescription = "Codi Logo",
                modifier = Modifier.size(120.dp)
            )
        }
    }
}

