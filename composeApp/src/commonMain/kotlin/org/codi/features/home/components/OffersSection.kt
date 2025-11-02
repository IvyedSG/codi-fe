package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.theme.CodiThemeValues

@Composable
fun OffersSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Ofertas",
            style = CodiThemeValues.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = CodiThemeValues.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OfferItem(
            icon = Icons.Default.Percent,
            text = "Descuento en tienda 10 %"
        )

        Spacer(modifier = Modifier.height(12.dp))

        OfferItem(
            icon = Icons.Default.ShoppingBag,
            text = "Bolsa reutilizable 200 g"
        )
    }
}

