package org.codi.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val CodiShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(12.dp), // Cambiado de 8.dp a 12.dp para coincidir con LoginScreen
    large = RoundedCornerShape(16.dp)
)
