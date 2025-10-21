package org.codi.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        // TopBar fijo - no hace scroll
        org.codi.common.components.TopBar(
            title = "Bienvenido a Codi"
        )

        // Contenido scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
        ) {
            // Card de Último Recibo
            LastReceiptCard(
                storeName = "Supermercado Tottus",
                date = "15 May 2025",
                amount = "S/ 75.50",
                co2Amount = "10,5 Kg"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de Recibos Verdes y CO₂ acumulado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Recibos Verdes
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Recibos Verdes",
                        style = CodiThemeValues.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "3",
                        style = CodiThemeValues.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 48.sp
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { /* Navegar a promos */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CodiThemeValues.colorScheme.tertiary,
                            contentColor = CodiThemeValues.colorScheme.onTertiary
                        ),
                        shape = CodiThemeValues.shapes.medium,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Ver Promos",
                            style = CodiThemeValues.typography.labelMedium,
                            color = LocalContentColor.current
                        )
                    }
                }

                // CO₂ acumulado
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CO₂ acumulado",
                        style = CodiThemeValues.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "10 Kg",
                        style = CodiThemeValues.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp
                        ),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Indicador verde
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(SecondaryGreen)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sección de Ofertas
            Text(
                text = "Ofertas",
                style = CodiThemeValues.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Ofertas
            OfferItem(
                icon = Icons.Default.Percent,
                text = "Descuento en tienda 10 %"
            )

            Spacer(modifier = Modifier.height(12.dp))

            OfferItem(
                icon = Icons.Default.ShoppingBag,
                text = "Bolsa reutilizable 200 g"
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón Escanear
                Button(
                    onClick = { /* Escanear */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodiThemeValues.colorScheme.secondary,
                        contentColor = CodiThemeValues.colorScheme.primary
                    ),
                    shape = CodiThemeValues.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Escanear",
                        tint = LocalContentColor.current,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Escanear",
                        style = CodiThemeValues.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = LocalContentColor.current
                    )
                }

                // Botón Ver Impacto
                Button(
                    onClick = { /* Ver impacto */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodiThemeValues.colorScheme.tertiary,
                        contentColor = CodiThemeValues.colorScheme.onTertiary
                    ),
                    shape = CodiThemeValues.shapes.medium,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = "Ver Impacto",
                        tint = LocalContentColor.current,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ver Impacto",
                        style = CodiThemeValues.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = LocalContentColor.current
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun LastReceiptCard(
    storeName: String,
    date: String,
    amount: String,
    co2Amount: String
) {
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
                        text = storeName,
                        style = CodiThemeValues.typography.bodyLarge,
                        color = CodiThemeValues.colorScheme.primary
                    )
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
                            text = amount,
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.primary.copy(alpha = 0.7f)
                        )
                    }
                }

                // Logo placeholder
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(CodiThemeValues.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Store,
                        contentDescription = "Logo",
                        tint = CodiThemeValues.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
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
                        text = co2Amount,
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

@Composable
fun OfferItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = CodiThemeValues.shapes.medium,
        color = CodiThemeValues.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CodiThemeValues.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = CodiThemeValues.typography.bodyMedium,
                color = CodiThemeValues.colorScheme.onBackground
            )
        }
    }
}
