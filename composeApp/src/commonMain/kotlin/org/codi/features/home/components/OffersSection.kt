package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.data.api.models.Promotion
import org.codi.theme.CodiThemeValues

@Composable
fun OffersSection(promociones: List<Promotion>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ofertas",
            style = CodiThemeValues.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = CodiThemeValues.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (promociones.isNotEmpty()) {
            promociones.forEach { promocion ->
                val icon = when (promocion.tipoPromocion.lowercase()) {
                    "descuento" -> Icons.Default.Percent
                    "producto" -> Icons.Default.ShoppingBag
                    else -> Icons.Default.LocalOffer
                }

                OfferItem(
                    icon = icon,
                    text = promocion.titulo
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        } else {
            Text(
                text = "No hay promociones disponibles",
                style = CodiThemeValues.typography.bodyMedium,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}

