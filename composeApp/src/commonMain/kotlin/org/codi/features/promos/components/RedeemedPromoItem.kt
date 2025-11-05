package org.codi.features.promos.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.data.api.models.Promocion
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

@Composable
fun RedeemedPromoItem(
    promocion: Promocion,
    onVerDetalle: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con logo de tienda y badge de "Canjeada"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo de tienda
                    Surface(
                        color = SecondaryGreen.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = null,
                                tint = SecondaryGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Text(
                        text = promocion.tienda?.nombre ?: "Tienda",
                        style = CodiThemeValues.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                }

                // Badge de "Canjeada"
                Surface(
                    color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Canjeada",
                            style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Título
            Text(
                text = promocion.titulo,
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tipo de promoción
            Surface(
                color = SecondaryGreen.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = promocion.tipoPromocion,
                    style = CodiThemeValues.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = SecondaryGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Fecha de uso si está disponible
            promocion.fechaUso?.let { fecha ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Canjeado: ${formatearFecha(fecha)}",
                        style = CodiThemeValues.typography.bodySmall,
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Botón para ver detalle
            Button(
                onClick = { onVerDetalle(promocion.id) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Ver Detalles",
                        style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }
}

/**
 * Formatea una fecha ISO 8601 a formato legible
 */
private fun formatearFecha(fechaISO: String): String {
    return try {
        val parts = fechaISO.split("T")[0].split("-")
        if (parts.size == 3) {
            val day = parts[2]
            val month = obtenerNombreMes(parts[1].toIntOrNull() ?: 1)
            val year = parts[0]
            "$day $month $year"
        } else {
            fechaISO.split("T").firstOrNull() ?: "Fecha no disponible"
        }
    } catch (_: Exception) {
        "Fecha no disponible"
    }
}

private fun obtenerNombreMes(mes: Int): String {
    return when (mes) {
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
        else -> "???"
    }
}

