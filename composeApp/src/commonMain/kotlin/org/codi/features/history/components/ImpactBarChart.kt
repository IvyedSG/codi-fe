package org.codi.features.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.codi.theme.PrimaryGreen

@Composable
fun ImpactBarChart() {
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
                count = "2",
                label = "Ejemplar",
                color = PrimaryGreen,
                height = 50.dp
            )

            BarItem(
                count = "1",
                label = "Regular",
                color = Color(0xFFFF9800),
                height = 35.dp
            )

            BarItem(
                count = "1",
                label = "Alto Impacto",
                color = Color(0xFFF44336),
                height = 35.dp
            )
        }
    }
}

