package org.codi.features.receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import org.codi.utils.formatDecimal
import org.codi.data.api.models.RecommendationsResponse
import org.codi.common.components.StoreLogo

data class ReceiptDetailScreen(
    val receiptId: String,
    val storeName: String = "TOTTUS",
    val fromUpload: Boolean = false
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = remember { ReceiptViewModel() }
        val detailState = viewModel.detailState
        val recState = viewModel.recommendationsState
        var showRecommendations by remember { mutableStateOf(false) }

        // Cargar datos al iniciar
        LaunchedEffect(receiptId) {
            viewModel.loadReceiptDetail(receiptId)
        }

        when (detailState) {
            is DetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CodiThemeValues.colorScheme.background),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryGreen
                    )
                }
            }
            is DetailState.Success -> {
                val boleta = detailState.response.data
                if (boleta != null) {
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

                            // Card del recibo con datos reales
                            ReceiptCard(boleta = boleta)

                            Spacer(modifier = Modifier.height(16.dp))

                            // Botón ver/ocultar recomendaciones
                            Button(
                                onClick = {
                                    showRecommendations = !showRecommendations
                                    if (showRecommendations) {
                                        viewModel.loadRecommendations(receiptId)
                                    }
                                },
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

                            // Mostrar recomendaciones si está activo: usar el estado separado de recomendaciones
                            if (showRecommendations) {
                                when (val rst = recState) {
                                    is RecommendationsState.Loading -> {
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                CircularProgressIndicator(color = PrimaryGreen)
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text("Generando recomendaciones... Esto puede tardar unos segundos", style = CodiThemeValues.typography.bodySmall, color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f))
                                            }
                                        }
                                    }
                                    is RecommendationsState.Success -> {
                                        val resp = rst.response
                                        RecommendationsSection(response = resp)
                                    }
                                    is RecommendationsState.Error -> {
                                        Text(
                                            text = "Error al obtener recomendaciones: ${rst.message}",
                                            style = CodiThemeValues.typography.bodyMedium,
                                            color = CodiThemeValues.colorScheme.error
                                        )
                                    }
                                    else -> {
                                        // Idle: no mostrar mensaje ni botón — al abrir la sección se invoca la carga y se mostrará el loader
                                    }
                                }

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
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            is DetailState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CodiThemeValues.colorScheme.background)
                ) {
                    org.codi.common.components.TopBar(
                        title = "Error"
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color(0xFFE53935),
                            modifier = Modifier.size(80.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Error al cargar",
                            style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = detailState.message,
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { navigator.pop() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CodiThemeValues.colorScheme.tertiary,
                                contentColor = CodiThemeValues.colorScheme.onTertiary
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text(
                                text = "Volver",
                                style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = LocalContentColor.current
                            )
                        }
                    }
                }
            }
            else -> {
                // Idle o estados no esperados
            }
        }
    }
}

@Composable
fun ReceiptCard(boleta: org.codi.data.api.models.BoletaDetalle) {
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
            // Logo y nombre del comercio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StoreLogo(
                    logoUrl = boleta.logoTienda,
                    storeName = boleta.nombreTienda,
                    modifier = Modifier.size(56.dp),
                    size = 56.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = boleta.nombreTienda.uppercase(),
                    style = CodiThemeValues.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = CodiThemeValues.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Tipo de boleta
            Text(
                text = "BOLETA DE VENTA ELECTRÓNICA",
                style = CodiThemeValues.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Fecha
            Text(
                text = "Fecha: ${boleta.fechaBoleta}",
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

            // Items del recibo (datos reales)
            boleta.productos.forEach { producto ->
                ReceiptItem(
                    description = producto.nombre,
                    quantity = producto.cantidad.formatDecimal(),
                    price = "S/ ${producto.precioTotal.formatDecimal()}",
                    co2 = "${producto.factorCo2.formatDecimal()} Kg",
                    isEco = producto.esLocal == true || producto.tieneEmpaqueEcologico == true,
                    isHighImpact = producto.factorCo2 > 2.0
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            // Total
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
                    text = "S/ ${boleta.total.formatDecimal()}",
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
                    text = "${boleta.analisis.co2Total.formatDecimal()} Kg",
                    style = CodiThemeValues.typography.titleMedium,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.2f))

            Spacer(modifier = Modifier.height(12.dp))

            // Resumen de productos eco-amigables
            val productosEco = boleta.productos.count { it.esLocal == true || it.tieneEmpaqueEcologico == true }
            val totalProductos = boleta.productos.size
            Text(
                text = "$productosEco Productos Eco-amigables de $totalProductos",
                style = CodiThemeValues.typography.bodySmall,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Tipo ambiental
            boleta.tipoAmbiental?.let { tipo ->
                val tipoAmbiental = org.codi.data.models.BoletaTipoAmbiental.fromString(tipo)
                Text(
                    text = "Recibo: ${org.codi.utils.TipoAmbientalUtils.getDisplayName(tipoAmbiental)}",
                    style = CodiThemeValues.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = org.codi.utils.TipoAmbientalUtils.getColor(tipoAmbiental),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
fun RecommendationsSection(response: RecommendationsResponse? = null) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (response == null || response.data == null) {
            // No hay recomendaciones (UI vacía). El botón para generarlas está en la pantalla de detalle.
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            }
        } else {
             // Mostrar lista real de recomendaciones
             val items = response.data.recomendaciones
             items.forEach { item ->
                 RecommendationCard(
                     productName = item.productoRecomendado.nombre,
                     co2Saved = "${item.mejora.co2Ahorrado.formatDecimal()} Kg de CO₂ vs ${item.productoOriginal.co2.formatDecimal()} Kg",
                     storeName = item.productoRecomendado.tienda ?: "-",
                     price = "-"
                 )
             }

             Spacer(modifier = Modifier.height(8.dp))

             // Resumen
             response.data.resumen.let { resumen ->
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
                             text = "${resumen.co2TotalAhorrable.formatDecimal()} Kg de CO₂ (${resumen.porcentajeMejoraPromedio.formatDecimal()}% menos)",
                             style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                             color = PrimaryGreen
                         )
                     }
                 }
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
