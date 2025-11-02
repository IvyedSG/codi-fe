package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeContent() {
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

        // Sección de Estadísticas
        StatsSection()

        Spacer(modifier = Modifier.height(32.dp))

        // Sección de Ofertas
        OffersSection()

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de acción
        ActionButtonsSection()

        Spacer(modifier = Modifier.height(32.dp))
    }
}

