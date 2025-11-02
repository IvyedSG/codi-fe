package org.codi.features.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

@Composable
fun ActivitySummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Resumen de actividad",
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tres círculos con estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCircle(
                    icon = Icons.Default.ShoppingCart,
                    value = "4",
                    label = "Compras",
                    backgroundColor = PrimaryGreen.copy(alpha = 0.15f),
                    iconTint = PrimaryGreen
                )

                StatCircle(
                    icon = Icons.Default.Eco,
                    value = "2",
                    label = "Ejemplares",
                    backgroundColor = PrimaryGreen.copy(alpha = 0.15f),
                    iconTint = PrimaryGreen
                )

                StatCircle(
                    icon = Icons.Default.Cloud,
                    value = "48.1 Kg",
                    label = "CO2 Total",
                    backgroundColor = PrimaryGreen.copy(alpha = 0.15f),
                    iconTint = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gráfico de barras
            ImpactBarChart()
        }
    }
}

