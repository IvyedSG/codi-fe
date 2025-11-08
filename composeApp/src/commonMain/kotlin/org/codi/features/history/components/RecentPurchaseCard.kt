package org.codi.features.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.data.api.models.RecentPurchase
import org.codi.data.models.BoletaTipoAmbiental
import org.codi.features.receipt.ReceiptDetailScreen
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.utils.TipoAmbientalUtils

@Composable
fun RecentPurchaseCard(purchase: RecentPurchase) {
    val navigator = LocalNavigator.currentOrThrow

    // Formatear la fecha - solo mostramos la fecha sin parsear por ahora
    val date = try {
        if (purchase.fechaBoleta.isNotEmpty()) {
            // Formato esperado: "2024-01-15T14:30:00Z"
            val parts = purchase.fechaBoleta.split("T")[0].split("-")
            if (parts.size == 3) {
                val day = parts[2]
                val month = getMonthName(parts[1].toIntOrNull() ?: 1)
                val year = parts[0]
                "$day $month $year"
            } else {
                purchase.fechaBoleta.split("T").firstOrNull() ?: "Fecha no disponible"
            }
        } else {
            "Fecha no disponible"
        }
    } catch (_: Exception) {
        "Fecha no disponible"
    }

    // Convertir el tipo de boleta a enum y obtener el color
    val tipoAmbiental = BoletaTipoAmbiental.fromString(purchase.tipoBoleta)
    val badgeColor = TipoAmbientalUtils.getColor(tipoAmbiental)

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
            // Header con logo y badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo de la tienda (inicial)
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = PrimaryGreen.copy(alpha = 0.15f)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = purchase.nombreTienda.firstOrNull()?.uppercase() ?: "T",
                                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryGreen
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = date,
                            style = CodiThemeValues.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                        Text(
                            text = purchase.nombreTienda,
                            style = CodiThemeValues.typography.bodySmall,
                            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                // Badge con tipo de boleta
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = badgeColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = TipoAmbientalUtils.getDisplayName(tipoAmbiental),
                            style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PurchaseStatItem(
                    icon = Icons.Default.Cloud,
                    value = "${(purchase.co2Boleta * 10).toInt() / 10.0} Kg",
                    label = "CO2 generado"
                )

                PurchaseStatItem(
                    icon = Icons.Default.Eco,
                    value = TipoAmbientalUtils.getDisplayName(tipoAmbiental),
                    label = "Calificación"
                )

                PurchaseStatItem(
                    icon = Icons.Default.ShoppingBag,
                    value = "${purchase.cantidadProductos} ítems",
                    label = "Productos"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Ver detalles
            OutlinedButton(
                onClick = {
                    navigator.push(ReceiptDetailScreen(
                        receiptId = purchase.id,
                        storeName = purchase.nombreTienda,
                        fromUpload = false
                    ))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(2.dp, PrimaryGreen),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryGreen
                )
            ) {
                Text(
                    text = "Ver detalles",
                    style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
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

