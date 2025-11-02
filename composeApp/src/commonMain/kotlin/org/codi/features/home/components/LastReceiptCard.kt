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
import org.codi.theme.CodiThemeValues

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

