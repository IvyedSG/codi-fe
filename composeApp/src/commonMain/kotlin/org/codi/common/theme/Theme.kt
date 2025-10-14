// Kotlin
package org.codi.theme

import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Surface
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.graphics.Color

@Composable
fun CodiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = if (darkTheme) {
        darkColorScheme(
            primary = PrimaryGreen,
            onPrimary = Color.White,
            primaryContainer = PrimaryGreen.copy(alpha = 0.15f),
            onPrimaryContainer = Color.White,
            secondary = SecondaryGreen,
            onSecondary = Color.White,
            secondaryContainer = SecondaryGreen.copy(alpha = 0.15f),
            onSecondaryContainer = Color.White,
            tertiary = ButtonDark, // Botón negro para acciones principales
            onTertiary = Color.White,
            surface = BackgroundDark,
            onSurface = Color.White,
            background = BackgroundDark,
            onBackground = Color.White,
            error = ErrorColor,
            onError = Color.White
        )
    } else {
        lightColorScheme(
            primary = PrimaryGreen,
            onPrimary = Color.White,
            primaryContainer = PrimaryGreen.copy(alpha = 0.1f),
            onPrimaryContainer = TextDark,
            secondary = SecondaryGreen,
            onSecondary = Color.White,
            secondaryContainer = SecondaryGreen.copy(alpha = 0.1f),
            onSecondaryContainer = Color.White,
            tertiary = ButtonDark, // Botón negro para acciones principales
            onTertiary = Color.White,
            surface = FieldBackground,
            onSurface = TextDark,
            background = BackgroundLight, // Fondo gris claro como en la imagen
            onBackground = TextDark,
            error = ErrorColor,
            onError = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CodiTypography,
        shapes = CodiShapes
    ) {
        Surface(color = colorScheme.background) {
            content()
        }
    }
}
