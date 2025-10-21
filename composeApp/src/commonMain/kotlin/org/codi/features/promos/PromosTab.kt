package org.codi.features.promos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

data class PromoCard(
    val title: String,
    val description: String,
    val icon: ImageVector
)

data class GreenReceiptPromo(
    val recibosRequired: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

object PromosTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Percent)
            return remember {
                TabOptions(
                    index = 1u,
                    title = "Promos",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        PromosScreen()
    }
}

@Composable
fun PromosScreen() {
    val destacadas = listOf(
        PromoCard("Descuento 20%", "En todas las frutas y verduras", Icons.Default.ShoppingBag),
        PromoCard("Eco bolsa gratis", "Por compras con recibos verdes", Icons.Default.LocalFlorist),
        PromoCard("2x1 en productos", "Seleccionados con recibo verde", Icons.Default.Recycling)
    )

    val recibosVerdes = listOf(
        GreenReceiptPromo(1, "1 Recibo Verde", "Descuento de S/10 en tu próxima compra", Icons.Default.AttachMoney),
        GreenReceiptPromo(2, "2 Recibos Verdes", "Bolsa ecológica reutilizable", Icons.Default.ShoppingBag)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        // TopBar fijo - no hace scroll
        org.codi.common.components.TopBar(
            title = "Promociones",
            showGreenReceipts = true,
            greenReceiptsCount = 7
        )

        // Contenido scrollable
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Banner principal
            item {
                FeaturedPromoBanner()
            }

            // Promociones destacadas
            item {
                Text(
                    text = "Promociones destacadas",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(destacadas) { promo ->
                        PromoCardItem(promo)
                    }
                }
            }

            // Exclusivo para Recibos Verdes
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = SecondaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Exclusivo para Recibos Verdes",
                        style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Lista de promociones de recibos verdes
            items(recibosVerdes) { promo ->
                GreenReceiptPromoItem(promo)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun FeaturedPromoBanner() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp)),
        color = SecondaryGreen.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Badge NOVEDAD
            Surface(
                color = Color(0xFF2D5F4F),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "NOVEDAD",
                        style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "¡Nuevas promociones!",
                style = CodiThemeValues.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Exclusivas para compras con recibos verdes",
                style = CodiThemeValues.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CodiThemeValues.colorScheme.tertiary,
                    contentColor = CodiThemeValues.colorScheme.onTertiary
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Ver todos",
                    style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = LocalContentColor.current
                )
            }
        }
    }
}

@Composable
fun PromoCardItem(promo: PromoCard) {
    Surface(
        modifier = Modifier
            .width(180.dp)
            .height(140.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icono
            Surface(
                color = SecondaryGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = promo.icon,
                        contentDescription = null,
                        tint = SecondaryGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = promo.title,
                    style = CodiThemeValues.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Text(
                    text = promo.description,
                    style = CodiThemeValues.typography.bodySmall,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun GreenReceiptPromoItem(promo: GreenReceiptPromo) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Icono
                Surface(
                    color = SecondaryGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = promo.icon,
                            contentDescription = null,
                            tint = SecondaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = promo.title,
                            style = CodiThemeValues.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SecondaryGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(
                        text = promo.description,
                        style = CodiThemeValues.typography.bodySmall,
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            // Botón Canjear
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = SecondaryGreen.copy(alpha = 0.3f),
                    contentColor = SecondaryGreen
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Canjear",
                    style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}
