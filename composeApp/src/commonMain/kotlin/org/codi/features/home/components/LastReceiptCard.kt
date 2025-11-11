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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.data.api.models.LastReceipt
import org.codi.features.receipt.ReceiptDetailScreen
import org.codi.theme.CodiThemeValues

@Composable
fun LastReceiptCard(lastReceipt: LastReceipt) {
    val navigator = LocalNavigator.currentOrThrow
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
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = lastReceipt.nombreTienda,
                        style = CodiThemeValues.typography.bodyLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )

                    // Solo mostrar categoría si no es null
                    lastReceipt.categoriaTienda?.let { categoria ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = categoria,
                            style = CodiThemeValues.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Fecha",
                            tint = CodiThemeValues.colorScheme.onBackground,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = date,
                            style = CodiThemeValues.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            imageVector = Icons.Default.AttachMoney,
                            contentDescription = "Monto",
                            tint = CodiThemeValues.colorScheme.onBackground,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "S/ ${(lastReceipt.precioTotal * 100).toInt() / 100.0}",
                            style = CodiThemeValues.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = CodiThemeValues.colorScheme.onBackground
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
                        color = CodiThemeValues.colorScheme.onBackground
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
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    Text(
                        text = "CO₂ generado",
                        style = CodiThemeValues.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                }

                Button(
                    onClick = {
                        // Navegar al detalle solo si existe el ID
                        lastReceipt.id?.let { boletaId ->
                            navigator.push(ReceiptDetailScreen(
                                receiptId = boletaId,
                                storeName = lastReceipt.nombreTienda,
                                fromUpload = false
                            ))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C2C2C),
                        contentColor = Color.White
                    ),
                    shape = CodiThemeValues.shapes.medium,
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Ver detalles",
                        style = CodiThemeValues.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
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

