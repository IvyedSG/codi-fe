package org.codi.features.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.codi.theme.CodiThemeValues

@Composable
fun HistoryContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
    ) {
        // Subtítulo
        Text(
            text = "Revisa tus compras anteriores y su impacto ambiental",
            style = CodiThemeValues.typography.bodyMedium,
            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Resumen de actividad
        ActivitySummaryCard()

        Spacer(modifier = Modifier.height(24.dp))

        // Compras recientes
        Text(
            text = "Compras recientes",
            style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tarjeta de compra reciente
        RecentPurchaseCard()

        Spacer(modifier = Modifier.height(24.dp))
    }
}

