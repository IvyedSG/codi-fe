package org.codi.features.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.codi.theme.PrimaryGreen
import kotlin.math.max

@Composable
fun ImpactBarChart(
    cantidadVerdes: Int,
    cantidadAmarillas: Int,
    cantidadRojas: Int
) {
    // Calcular altura proporcional (mínimo 30dp, máximo 60dp)
    val maxValue = max(max(cantidadVerdes, cantidadAmarillas), cantidadRojas)
    val baseHeight = 30.dp
    val maxHeight = 60.dp

    val heightVerdes = if (maxValue > 0) {
        baseHeight + ((cantidadVerdes.toFloat() / maxValue) * (maxHeight.value - baseHeight.value)).dp
    } else baseHeight

    val heightAmarillas = if (maxValue > 0) {
        baseHeight + ((cantidadAmarillas.toFloat() / maxValue) * (maxHeight.value - baseHeight.value)).dp
    } else baseHeight

    val heightRojas = if (maxValue > 0) {
        baseHeight + ((cantidadRojas.toFloat() / maxValue) * (maxHeight.value - baseHeight.value)).dp
    } else baseHeight

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF5F5F5)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BarItem(
                count = cantidadVerdes.toString(),
                label = "Verdes",
                color = PrimaryGreen,
                height = heightVerdes
            )

            BarItem(
                count = cantidadAmarillas.toString(),
                label = "Amarillas",
                color = Color(0xFFFF9800),
                height = heightAmarillas
            )

            BarItem(
                count = cantidadRojas.toString(),
                label = "Rojas",
                color = Color(0xFFF44336),
                height = heightRojas
            )
        }
    }
}

