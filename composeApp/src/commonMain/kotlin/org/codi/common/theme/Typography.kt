package org.codi.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp

val PrimaryFontFamily = FontFamily.Default
val SecondaryFontFamily = FontFamily.Default

val CodiTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = SecondaryFontFamily,
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark
    ),
    displayMedium = TextStyle(
        fontFamily = SecondaryFontFamily,
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark
    ),
    displaySmall = TextStyle(
        fontFamily = SecondaryFontFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = TextDark
    ),
    headlineLarge = TextStyle(
        fontFamily = SecondaryFontFamily,
        fontSize = 22.sp,
        fontWeight = FontWeight.W600,
        color = TextDark
    ),
    headlineMedium = TextStyle(
        fontFamily = SecondaryFontFamily,
        fontSize = 20.sp,
        fontWeight = FontWeight.W600,
        color = TextDark
    ),
    headlineSmall = TextStyle(
        fontFamily = SecondaryFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.W600,
        color = TextDark
    ),
    titleLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.W500,
        color = TextDark
    ),
    titleMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.W500,
        color = TextDark
    ),
    bodyLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = TextDark
    ),
    bodyMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontSize = 16.sp, // Cambiado de 14.sp a 16.sp para el texto "Cada recibo deja una huella"
        fontWeight = FontWeight.Normal,
        color = TextDark
    ),
    bodySmall = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontSize = 14.sp, // Cambiado de 12.sp a 14.sp para el texto "¿Todavía no tienes una cuenta?"
        fontWeight = FontWeight.Normal,
        color = TextDark
    ),
    labelLarge = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontSize = 16.sp, // Cambiado de 14.sp a 16.sp para el botón "Ingresar"
        fontWeight = FontWeight.Medium,
        color = TextDark
    ),
    labelMedium = TextStyle(
        fontFamily = PrimaryFontFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = TextDark
    )
)
