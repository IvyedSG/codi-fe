package org.codi.features.history.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.codi.data.api.models.HistoryResponse
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

@Composable
fun HistoryContent(
    historyData: HistoryResponse,
    isRefreshing: Boolean = false
) {
    val activitySummary = historyData.data?.resumenActividad
    val recentPurchases = historyData.data?.comprasRecientes ?: emptyList()

    Box(modifier = Modifier.fillMaxSize()) {
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

            // Resumen de actividad con datos del endpoint
            if (activitySummary != null) {
                ActivitySummaryCard(activitySummary = activitySummary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Compras recientes
            if (recentPurchases.isNotEmpty()) {
                Text(
                    text = "Compras recientes",
                    style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Mostrar todas las compras recientes
                recentPurchases.forEach { purchase ->
                    RecentPurchaseCard(purchase = purchase)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            } else {
                Text(
                    text = "No hay compras recientes",
                    style = CodiThemeValues.typography.bodyMedium,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Indicador de carga flotante cuando está refrescando
        if (isRefreshing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                CircularProgressIndicator(
                    color = PrimaryGreen,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

