package org.codi.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.data.api.models.LastReceipt
import org.codi.theme.CodiThemeValues

@Composable
fun LastReceiptCard(lastReceipt: LastReceipt) {
    // Formatear la fecha
    val date = try {
        if (lastReceipt.fechaBoleta.isNotEmpty()) {
            val parts = lastReceipt.fechaBoleta.split("T")[0].split("-")
            if (parts.size == 3) {
                val day = parts[2]
                val month = getMonthName(parts[1].toIntOrNull() ?: 1)
                val year = parts[0]
                "$day $month $year"
            } else {
                lastReceipt.fechaBoleta.split("T").firstOrNull() ?: "Fecha no disponible"
            }
        } else {
            "Fecha no disponible"
        }
    } catch (_: Exception) {
        "Fecha no disponible"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = CodiThemeValues.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = CodiThemeValues.colorScheme.secondary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "Último Recibo",
                        style = CodiThemeValues.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = CodiThemeValues.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = lastReceipt.nombreTienda,
                        style = CodiThemeValues.typography.bodyLarge,
                        color = CodiThemeValues.colorScheme.primary
                    )

                    // Solo mostrar categoría si no es null
                    lastReceipt.categoriaTienda?.let { categoria ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = categoria,
                            style = CodiThemeValues.typography.bodySmall,
                            color = CodiThemeValues.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Fecha",
                            tint = CodiThemeValues.colorScheme.primary.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = date,
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.primary.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Monto",
                            tint = CodiThemeValues.colorScheme.primary.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "S/ ${(lastReceipt.precioTotal * 100).toInt() / 100.0}",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }
                }

                // Logo placeholder con inicial de la tienda
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(CodiThemeValues.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = lastReceipt.nombreTienda.firstOrNull()?.uppercase() ?: "T",
                        style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = CodiThemeValues.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${(lastReceipt.co2Total * 10).toInt() / 10.0} Kg",
                        style = CodiThemeValues.typography.displaySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = CodiThemeValues.colorScheme.primary
                    )
                    Text(
                        text = "CO₂ generado",
                        style = CodiThemeValues.typography.bodySmall,
                        color = CodiThemeValues.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }

                Button(
                    onClick = { /* Ver detalles */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodiThemeValues.colorScheme.tertiary,
                        contentColor = CodiThemeValues.colorScheme.onTertiary
                    ),
                    shape = CodiThemeValues.shapes.medium,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Ver detalles",
                        style = CodiThemeValues.typography.labelMedium,
                        color = LocalContentColor.current
                    )
                }
            }
        }
    }
}

// Función auxiliar para obtener el nombre del mes en español
private fun getMonthName(month: Int): String {
    return when (month) {
        1 -> "Ene"
        2 -> "Feb"
        3 -> "Mar"
        4 -> "Abr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Ago"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"
        12 -> "Dic"
        else -> ""
    }
}

