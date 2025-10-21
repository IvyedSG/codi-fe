package org.codi.features.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.codi.features.receipt.ReceiptDetailScreen
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

object HistoryTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Receipt)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "Historial",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(HistoryTabScreen())
    }
}

class HistoryTabScreen : Screen {
    @Composable
    override fun Content() {
        HistoryScreen()
    }
}

@Composable
fun HistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        // TopBar estándar
        org.codi.common.components.TopBar(
            title = "Historial"
        )

        // Contenido scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
        ) {
            // Subtítulo
            Text(
                text = "Revisa tus compras anteriores y su impacto ambiental",
                style = CodiThemeValues.typography.bodyMedium,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resumen de actividad
            ActivitySummaryCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Compras recientes
            Text(
                text = "Compras recientes",
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Tarjeta de compra reciente
            RecentPurchaseCard()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

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

@Composable
fun StatCircle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    backgroundColor: Color,
    iconTint: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Círculo con icono
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = backgroundColor
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Valor
        Text(
            text = value,
            style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )

        // Label
        Text(
            text = label,
            style = CodiThemeValues.typography.bodySmall,
            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

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

@Composable
fun BarItem(
    count: String,
    label: String,
    color: Color,
    height: Dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Barra
        Surface(
            modifier = Modifier
                .width(50.dp)
                .height(height),
            shape = RoundedCornerShape(6.dp),
            color = color
        ) {}

        // Número
        Text(
            text = count,
            style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )

        // Label
        Text(
            text = label,
            style = CodiThemeValues.typography.bodySmall,
            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

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

@Composable
fun PurchaseStatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = value,
            style = CodiThemeValues.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )

        Text(
            text = label,
            style = CodiThemeValues.typography.bodySmall,
            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

