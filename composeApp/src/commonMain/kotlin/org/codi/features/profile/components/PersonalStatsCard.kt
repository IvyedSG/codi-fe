package org.codi.features.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.theme.CodiThemeValues

@Composable
fun PersonalStatsCard(
    cantidadRecibos: Int,
    cantidadRecibosVerdes: Int,
    co2Total: Double,
    co2Promedio: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "Mis Estadísticas",
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Una sola fila con los 4 datos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Receipt,
                    value = cantidadRecibos.toString(),
                    label = "Recibos Totales"
                )
                StatItem(
                    icon = Icons.Default.Eco,
                    value = cantidadRecibosVerdes.toString(),
                    label = "Recibos Verdes"
                )
                StatItem(
                    icon = Icons.Default.Cloud,
                    value = "${(co2Total * 10).toInt() / 10.0}",
                    label = "CO2 Total"
                )
                StatItem(
                    icon = Icons.Default.Analytics,
                    value = "${(co2Promedio * 10).toInt() / 10.0}",
                    label = "CO2 Promedio"
                )
            }
        }
    }
}
