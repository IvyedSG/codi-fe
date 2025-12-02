package org.codi.features.receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.data.api.models.ProductoBoleta
import org.codi.theme.CodiThemeValues
import org.codi.utils.formatDecimal

data class EnvironmentalRangeScreen(
    val product: ProductoBoleta
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val rangosAmbientales = product.rangosAmbientales

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFf9fafb))
        ) {
            // Top Bar
            Surface(
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = { navigator.pop() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color(0xFF1f2937)
                        )
                    }
                    Text(
                        text = "Rango Ambiental",
                        style = CodiThemeValues.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1f2937)
                    )
                }
            }

            // Contenido scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Sección educativa: ¿Qué es el rango ambiental?
                InfoCard(
                    icon = Icons.Default.Info,
                    iconColor = Color(0xFF3b82f6),
                    title = "¿Qué es el Rango Ambiental?",
                    description = "El rango ambiental te ayuda a entender el impacto de CO₂ de este producto comparado con otros de su misma categoría. Te mostramos dónde está ubicado tu producto en una escala de bajo, moderado y alto impacto."
                )

                // Información del producto
                ProductInfoCard(product = product)

                if (rangosAmbientales != null) {
                    val tuPosicion = rangosAmbientales.tuPosicion
                    val zonaColor = when (tuPosicion.zona.uppercase()) {
                        "VERDE" -> Color(0xFF22c55e)
                        "AMARILLO" -> Color(0xFFf59e0b)
                        "ROJO" -> Color(0xFFef4444)
                        else -> Color.Gray
                    }

                    // Huella de carbono del producto
                    CarbonFootprintCard(
                        co2Value = product.factorCo2,
                        zone = tuPosicion.zona,
                        zoneColor = zonaColor,
                        percentage = tuPosicion.porcentajeEnZona,
                        message = tuPosicion.mensaje
                    )

                    // Visualización de rangos
                    RangeVisualizationCard(
                        subcategoria = rangosAmbientales.subcategoria,
                        rangos = rangosAmbientales.rangos,
                        tuPosicion = tuPosicion,
                        productCo2 = product.factorCo2
                    )

                    // Datos de la categoría
                    CategoryDataCard(
                        subcategoria = rangosAmbientales.subcategoria,
                        huellaMedia = rangosAmbientales.huella_media_kg_co2_por_kg,
                        rangoMin = rangosAmbientales.rango_min,
                        rangoMax = rangosAmbientales.rango_max
                    )

                    // Tips para reducir impacto
                    TipsCard(zone = tuPosicion.zona)
                } else {
                    // Sin datos
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Color(0xFF6b7280),
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No hay datos de rango ambiental disponibles para este producto",
                                style = CodiThemeValues.typography.bodyMedium,
                                color = Color(0xFF6b7280),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun InfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = iconColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = CodiThemeValues.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1f2937)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = CodiThemeValues.typography.bodySmall,
                    color = Color(0xFF6b7280),
                    lineHeight = CodiThemeValues.typography.bodySmall.fontSize * 1.4
                )
            }
        }
    }
}

@Composable
private fun ProductInfoCard(product: ProductoBoleta) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Producto",
                style = CodiThemeValues.typography.labelMedium,
                color = Color(0xFF6b7280)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.nombre,
                style = CodiThemeValues.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1f2937)
            )
            if (product.subcategoria != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = Color(0xFF22c55e),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = product.subcategoria,
                        style = CodiThemeValues.typography.bodyMedium,
                        color = Color(0xFF22c55e)
                    )
                }
            }
        }
    }
}

@Composable
private fun CarbonFootprintCard(
    co2Value: Double,
    zone: String,
    zoneColor: Color,
    percentage: Double,
    message: String
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Tu Producto",
                style = CodiThemeValues.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1f2937)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Huella de CO2
                Surface(
                    color = Color(0xFFe0f2fe),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cloud,
                            contentDescription = null,
                            tint = Color(0xFF0369a1),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = co2Value.formatDecimal(),
                            style = CodiThemeValues.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF0c4a6e)
                        )
                        Text(
                            text = "Kg CO₂",
                            style = CodiThemeValues.typography.labelMedium,
                            color = Color(0xFF0369a1)
                        )
                    }
                }

                // Zona
                Surface(
                    color = zoneColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, zoneColor),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(zoneColor)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = zone,
                            style = CodiThemeValues.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = zoneColor
                        )
                        Text(
                            text = "${percentage.formatDecimal(0)}%",
                            style = CodiThemeValues.typography.labelMedium,
                            color = Color(0xFF4b5563)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje
            Surface(
                color = Color(0xFFf9fafb),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF6b7280),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = message,
                        style = CodiThemeValues.typography.bodyMedium,
                        color = Color(0xFF4b5563)
                    )
                }
            }
        }
    }
}

@Composable
private fun RangeVisualizationCard(
    subcategoria: String,
    rangos: org.codi.data.api.models.RangosAmbientalesData,
    tuPosicion: org.codi.data.api.models.TuPosicion,
    productCo2: Double
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Rangos de Impacto",
                style = CodiThemeValues.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1f2937)
            )
            Text(
                text = "Categoría: $subcategoria",
                style = CodiThemeValues.typography.bodySmall,
                color = Color(0xFF6b7280)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Rango Verde
            RangeItem(
                range = rangos.verde,
                color = Color(0xFF22c55e),
                isCurrentZone = tuPosicion.zona.uppercase() == "VERDE",
                productValue = if (tuPosicion.zona.uppercase() == "VERDE") productCo2 else null
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rango Amarillo
            RangeItem(
                range = rangos.amarillo,
                color = Color(0xFFf59e0b),
                isCurrentZone = tuPosicion.zona.uppercase() == "AMARILLO",
                productValue = if (tuPosicion.zona.uppercase() == "AMARILLO") productCo2 else null
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rango Rojo
            RangeItem(
                range = rangos.rojo,
                color = Color(0xFFef4444),
                isCurrentZone = tuPosicion.zona.uppercase() == "ROJO",
                productValue = if (tuPosicion.zona.uppercase() == "ROJO") productCo2 else null
            )
        }
    }
}

@Composable
private fun RangeItem(
    range: org.codi.data.api.models.RangoZona,
    color: Color,
    isCurrentZone: Boolean,
    productValue: Double?
) {
    Surface(
        color = if (isCurrentZone) color.copy(alpha = 0.12f) else Color(0xFFf9fafb),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCurrentZone) 2.dp else 1.dp,
            color = if (isCurrentZone) color else Color(0xFFe5e7eb)
        ),
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
                        .size(16.dp)
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
                        "${range.min.formatDecimal()} - ${range.max.formatDecimal()} Kg CO₂"
                    } else {
                        "> ${range.min.formatDecimal()} Kg CO₂"
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
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = productValue.formatDecimal(),
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
}

@Composable
private fun CategoryDataCard(
    subcategoria: String,
    huellaMedia: Double,
    rangoMin: Double,
    rangoMax: Double
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = Color(0xFF8b5cf6),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Datos de la Categoría",
                    style = CodiThemeValues.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1f2937)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                color = Color(0xFFf9fafb),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DataRow(
                        label = "Categoría:",
                        value = subcategoria
                    )
                    DataRow(
                        label = "Huella promedio:",
                        value = "${huellaMedia.formatDecimal()} Kg CO₂"
                    )
                    DataRow(
                        label = "Rango completo:",
                        value = "${rangoMin.formatDecimal()} - ${rangoMax.formatDecimal()} Kg CO₂"
                    )
                }
            }
        }
    }
}

@Composable
private fun DataRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = CodiThemeValues.typography.bodyMedium,
            color = Color(0xFF6b7280)
        )
        Text(
            text = value,
            style = CodiThemeValues.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color(0xFF1f2937)
        )
    }
}

@Composable
private fun TipsCard(zone: String) {
    val (icon, iconColor, title, tips) = when (zone.uppercase()) {
        "VERDE" -> Quadruple(
            Icons.Default.EmojiEvents,
            Color(0xFF22c55e),
            "¡Excelente elección! 🎉",
            listOf(
                "Este producto tiene bajo impacto ambiental",
                "Estás contribuyendo a reducir tu huella de carbono",
                "Sigue eligiendo productos en zona verde"
            )
        )
        "AMARILLO" -> Quadruple(
            Icons.Default.Lightbulb,
            Color(0xFFf59e0b),
            "Puedes mejorar 💡",
            listOf(
                "Este producto tiene impacto moderado",
                "Busca alternativas en zona verde",
                "Consulta las recomendaciones de productos similares"
            )
        )
        else -> Quadruple(
            Icons.Default.Warning,
            Color(0xFFef4444),
            "Considera alternativas ⚠️",
            listOf(
                "Este producto tiene alto impacto ambiental",
                "Te recomendamos buscar alternativas más ecológicas",
                "Revisa las recomendaciones para reducir tu huella"
            )
        )
    }

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = CodiThemeValues.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1f2937)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            tips.forEach { tip ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(iconColor)
                    )
                    Text(
                        text = tip,
                        style = CodiThemeValues.typography.bodyMedium,
                        color = Color(0xFF4b5563),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

// Helper class for Tips data
private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)

