package org.codi.features.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRightAlt
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
import org.codi.features.receipt.ReceiptDetailScreen
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

@Composable
fun RecentPurchaseCard() {
    val navigator = LocalNavigator.currentOrThrow

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
                    // Logo de Tottus (simulado con un círculo con texto)
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
                                text = "T",
                                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = PrimaryGreen
                            )
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "15 May 2025",
                            style = CodiThemeValues.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                        Text(
                            text = "Tottus",
                            style = CodiThemeValues.typography.bodySmall,
                            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                // Badge VERDE
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PrimaryGreen
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
                            text = "VERDE",
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
                    value = "6.3 Kg",
                    label = "CO2 generado"
                )

                PurchaseStatItem(
                    icon = Icons.Default.Eco,
                    value = "Ejemplar",
                    label = "Calificación"
                )

                PurchaseStatItem(
                    icon = Icons.Default.ShoppingBag,
                    value = "7 ítems",
                    label = "Productos"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Ver detalles
            OutlinedButton(
                onClick = {
                    navigator.push(ReceiptDetailScreen(
                        receiptId = "1",
                        storeName = "Tottus",
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
                    imageVector = Icons.Default.ArrowRightAlt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

