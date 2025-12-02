package org.codi.features.receipt.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.codi.data.api.models.ProductoBoleta
import org.codi.theme.CodiThemeValues
import org.codi.utils.formatDecimal

@Composable
fun EnvironmentalTraceDialog(
    product: ProductoBoleta,
    onDismiss: () -> Unit
) {
    val rangosAmbientales = product.rangosAmbientales

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header con botón cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = Color(0xFF22c55e),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Rastro Ambiental",
                            style = CodiThemeValues.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1f2937)
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color(0xFF6b7280),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Nombre del producto
                Text(
                    text = product.nombre,
                    style = CodiThemeValues.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF1f2937),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subcategoría
                if (product.subcategoria != null) {
                    Text(
                        text = product.subcategoria,
                        style = CodiThemeValues.typography.labelMedium,
                        color = Color(0xFF6b7280)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (rangosAmbientales == null) {
                    // Sin datos ambientales
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = CodiThemeValues.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = CodiThemeValues.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No hay datos ambientales disponibles para este producto",
                                style = CodiThemeValues.typography.bodyMedium,
                                color = CodiThemeValues.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    // Tu posición
                    val tuPosicion = rangosAmbientales.tuPosicion
                    val zonaColor = when (tuPosicion.zona.uppercase()) {
                        "VERDE" -> Color(0xFF22c55e)
                        "AMARILLO" -> Color(0xFFf59e0b)
                        "ROJO" -> Color(0xFFef4444)
                        else -> Color.Gray
                    }

                    // Row con huella y zona lado a lado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Información de huella de carbono
                        Surface(
                            color = Color(0xFFe0f2fe),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Huella CO₂",
                                    style = CodiThemeValues.typography.labelMedium,
                                    color = Color(0xFF0369a1)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${product.factorCo2.formatDecimal()}",
                                    style = CodiThemeValues.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF0c4a6e)
                                )
                                Text(
                                    text = "Kg CO₂",
                                    style = CodiThemeValues.typography.labelSmall,
                                    color = Color(0xFF0369a1)
                                )
                            }
                        }

                        // Zona de impacto
                        Surface(
                            color = zonaColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, zonaColor),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(zonaColor)
                                    )
                                    Text(
                                        text = "Zona",
                                        style = CodiThemeValues.typography.labelMedium,
                                        color = Color(0xFF4b5563)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = tuPosicion.zona,
                                    style = CodiThemeValues.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = zonaColor
                                )
                                Text(
                                    text = "${tuPosicion.porcentajeEnZona.formatDecimal(0)}%",
                                    style = CodiThemeValues.typography.labelSmall,
                                    color = Color(0xFF4b5563)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mensaje de zona
                    Text(
                        text = tuPosicion.mensaje,
                        style = CodiThemeValues.typography.bodyMedium,
                        color = Color(0xFF4b5563),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rangos visuales
                    Text(
                        text = "Comparativa - ${rangosAmbientales.subcategoria}",
                        style = CodiThemeValues.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1f2937)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Rango Verde
                    RangeCard(
                        range = rangosAmbientales.rangos.verde,
                        color = Color(0xFF22c55e),
                        isCurrentZone = tuPosicion.zona.uppercase() == "VERDE",
                        productValue = if (tuPosicion.zona.uppercase() == "VERDE") product.factorCo2 else null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rango Amarillo
                    RangeCard(
                        range = rangosAmbientales.rangos.amarillo,
                        color = Color(0xFFf59e0b),
                        isCurrentZone = tuPosicion.zona.uppercase() == "AMARILLO",
                        productValue = if (tuPosicion.zona.uppercase() == "AMARILLO") product.factorCo2 else null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rango Rojo
                    RangeCard(
                        range = rangosAmbientales.rangos.rojo,
                        color = Color(0xFFef4444),
                        isCurrentZone = tuPosicion.zona.uppercase() == "ROJO",
                        productValue = if (tuPosicion.zona.uppercase() == "ROJO") product.factorCo2 else null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Promedio de categoría
                    HorizontalDivider(color = Color(0xFFe5e7eb))

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = Color(0xFFf9fafb),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Promedio categoría:",
                                    style = CodiThemeValues.typography.bodySmall,
                                    color = Color(0xFF6b7280)
                                )
                                Text(
                                    text = "${rangosAmbientales.huella_media_kg_co2_por_kg.formatDecimal()} Kg",
                                    style = CodiThemeValues.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color(0xFF374151)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Rango categoría:",
                                    style = CodiThemeValues.typography.bodySmall,
                                    color = Color(0xFF6b7280)
                                )
                                Text(
                                    text = "${rangosAmbientales.rango_min.formatDecimal()} - ${rangosAmbientales.rango_max.formatDecimal()} Kg",
                                    style = CodiThemeValues.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Color(0xFF374151)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón cerrar
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22c55e)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Entendido",
                        style = CodiThemeValues.typography.labelLarge,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RangeCard(
    range: org.codi.data.api.models.RangoZona,
    color: Color,
    isCurrentZone: Boolean,
    productValue: Double?
) {
    Surface(
        color = if (isCurrentZone) color.copy(alpha = 0.12f) else Color(0xFFf9fafb),
        shape = RoundedCornerShape(10.dp),
        border = if (isCurrentZone) {
            androidx.compose.foundation.BorderStroke(2.dp, color)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFe5e7eb))
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Column {
                    Text(
                        text = range.label,
                        style = CodiThemeValues.typography.bodyMedium.copy(
                            fontWeight = if (isCurrentZone) FontWeight.Bold else FontWeight.SemiBold
                        ),
                        color = if (isCurrentZone) color else Color(0xFF374151)
                    )
                    val rangeText = if (range.max != null) {
                        "${range.min.formatDecimal()} - ${range.max.formatDecimal()} Kg"
                    } else {
                        "> ${range.min.formatDecimal()} Kg"
                    }
                    Text(
                        text = rangeText,
                        style = CodiThemeValues.typography.labelSmall,
                        color = Color(0xFF6b7280)
                    )
                }
            }

            if (isCurrentZone && productValue != null) {
                Surface(
                    color = color,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${productValue.formatDecimal()}",
                        style = CodiThemeValues.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

