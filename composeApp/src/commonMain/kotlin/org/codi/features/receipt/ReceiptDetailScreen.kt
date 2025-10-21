package org.codi.features.receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.theme.SecondaryGreen

data class ReceiptDetailScreen(
    val receiptId: String,
    val storeName: String = "TOTTUS",
    val fromUpload: Boolean = false
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showRecommendations by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodiThemeValues.colorScheme.background)
        ) {
            // TopBar fijo
            org.codi.common.components.TopBar(
                title = "Detalle de Recibo"
            )

            // Contenido scrollable
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Card del recibo
                ReceiptCard(storeName = storeName)

                Spacer(modifier = Modifier.height(16.dp))

                // Botón ver/ocultar recomendaciones
                Button(
                    onClick = { showRecommendations = !showRecommendations },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryGreen,
                        contentColor = Color.White
                    ),
                    shape = CodiThemeValues.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (showRecommendations) "Ocultar recomendaciones" else "Ver recomendaciones eco",
                        style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Mostrar recomendaciones si está activo
                if (showRecommendations) {
                    RecommendationsSection()
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Botón Volver
                Button(
                    onClick = { navigator.pop() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodiThemeValues.colorScheme.tertiary,
                        contentColor = CodiThemeValues.colorScheme.onTertiary
                    ),
                    shape = CodiThemeValues.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Volver",
                        style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = LocalContentColor.current
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ReceiptCard(storeName: String = "TOTTUS") {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Nombre del comercio
            Text(
                text = storeName,
                style = CodiThemeValues.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black
                ),
                color = CodiThemeValues.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Dirección
            Text(
                text = "AV. JAVIER PRADO ESTE 123, SAN BORJA",
                style = CodiThemeValues.typography.labelSmall,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(2.dp))

            // RUC
            Text(
                text = "RUC: 20123456789",
                style = CodiThemeValues.typography.labelSmall,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tipo de boleta
            Text(
                text = "BOLETA DE VENTA ELECTRÓNICA",
                style = CodiThemeValues.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Número de boleta
            Text(
                text = "B007 00123456",
                style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = CodiThemeValues.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Fecha y hora
            Text(
                text = "Fecha: 22/05/2025  Hora: 15:30",
                style = CodiThemeValues.typography.labelSmall,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.15f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Headers de tabla
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "DESCRIPCIÓN",
                    style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground,
                    modifier = Modifier.weight(2f)
                )
                Text(
                    text = "CANT",
                    style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground,
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "PRECIO",
                    style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground,
                    modifier = Modifier.weight(0.7f),
                    textAlign = TextAlign.End
                )
                Text(
                    text = "CO₂",
                    style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground,
                    modifier = Modifier.weight(0.6f),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Items del recibo
            ReceiptItem("MANZANA NACIONAL", "0.750", "S/ 5.93", "0.3 Kg", isEco = true)
            ReceiptItem("PLÁTANO ORGÁNICO", "1.000", "S/ 5.50", "0.2 Kg", isEco = true)
            ReceiptItem("LECHE DESLAC X 1L", "2", "S/ 11.8", "1.8 Kg", isEco = false, isHighImpact = true)
            ReceiptItem("PAN INTEGRAL", "1", "S/ 8.9", "0.5 Kg", isEco = true)
            ReceiptItem("HAMBURGUESA CARNE", "1", "S/ 15.9", "2.1 Kg", isEco = false, isHighImpact = true)
            ReceiptItem("PAPEL TOALLA ECO", "1", "S/ 12.5", "0.6 Kg", isEco = true)
            ReceiptItem("DETERGENTE REGULAR", "1", "S/ 18.9", "1.1 Kg", isEco = false)

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            // Totales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SUBTOTAL:",
                    style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Text(
                    text = "S/ 98.40",
                    style = CodiThemeValues.typography.bodyMedium,
                    color = CodiThemeValues.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "IGV (18%):",
                    style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Text(
                    text = "S/ 21.60",
                    style = CodiThemeValues.typography.bodyMedium,
                    color = CodiThemeValues.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL:",
                    style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Text(
                    text = "S/ 120.00",
                    style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL CO₂:",
                    style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Text(
                    text = "6.0 Kg",
                    style = CodiThemeValues.typography.titleMedium,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            // Método de pago
            Text(
                text = "VISA ****3456",
                style = CodiThemeValues.typography.bodySmall,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Productos eco-amigables
            Text(
                text = "4 Productos Eco-amigables de 7",
                style = CodiThemeValues.typography.bodySmall,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ReceiptItem(
    description: String,
    quantity: String,
    price: String,
    co2: String,
    isEco: Boolean = false,
    isHighImpact: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(2f)
        ) {
            if (isEco) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(12.dp)
                )
            }
            Text(
                text = description,
                style = CodiThemeValues.typography.bodySmall,
                color = if (isEco) PrimaryGreen else CodiThemeValues.colorScheme.onBackground
            )
        }
        Text(
            text = quantity,
            style = CodiThemeValues.typography.bodySmall,
            color = CodiThemeValues.colorScheme.onBackground,
            modifier = Modifier.weight(0.5f),
            textAlign = TextAlign.Center
        )
        Text(
            text = price,
            style = CodiThemeValues.typography.bodySmall,
            color = CodiThemeValues.colorScheme.onBackground,
            modifier = Modifier.weight(0.7f),
            textAlign = TextAlign.End
        )
        Text(
            text = co2,
            style = CodiThemeValues.typography.bodySmall.copy(fontWeight = if (isHighImpact) FontWeight.Bold else FontWeight.Normal),
            color = if (isHighImpact) Color(0xFFE53935) else if (isEco) PrimaryGreen else CodiThemeValues.colorScheme.onBackground,
            modifier = Modifier.weight(0.6f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun RecommendationsSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Recomendación 1
        RecommendationCard(
            productName = "Leche de almendras NaturGreen",
            co2Saved = "0.5 Kg de CO₂ vs 1.8 Kg",
            storeName = "Wong",
            price = "S/ 9.90"
        )

        // Recomendación 2
        RecommendationCard(
            productName = "Hamburguesa NotCo Plant Based",
            co2Saved = "0.4 Kg de CO₂ vs 2.1 Kg",
            storeName = "Plaza Vea",
            price = "S/ 22.50"
        )

        // Recomendación 3
        RecommendationCard(
            productName = "Detergente Ecológico Ecover",
            co2Saved = "0.3 Kg de CO₂ vs 1.1 Kg",
            storeName = "Tottus",
            price = "S/ 20.50"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Card de ahorro potencial
        Surface(
            color = SecondaryGreen.copy(alpha = 0.15f),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ahorro potencial de CO₂:",
                    style = CodiThemeValues.typography.bodyMedium,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "3.2 Kg de CO₂ (53% menos)",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryGreen
                )
            }
        }
    }
}

@Composable
fun RecommendationCard(
    productName: String,
    co2Saved: String,
    storeName: String,
    price: String
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SecondaryGreen.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = null,
                tint = PrimaryGreen,
                modifier = Modifier.size(24.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = productName,
                    style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = co2Saved,
                    style = CodiThemeValues.typography.bodySmall,
                    color = PrimaryGreen
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = storeName,
                            style = CodiThemeValues.typography.labelSmall,
                            color = Color(0xFFE53935),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = price,
                        style = CodiThemeValues.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

