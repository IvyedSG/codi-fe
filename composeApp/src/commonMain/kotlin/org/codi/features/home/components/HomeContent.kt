package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codi.data.api.models.HomeResponse
import org.codi.theme.PrimaryGreen

@Composable
fun HomeContent(
    homeData: HomeResponse,
    isRefreshing: Boolean = false,
    onEscanearClick: () -> Unit,
    onVerImpactoClick: () -> Unit,
    onVerPromosClick: () -> Unit
) {
    val data = homeData.data
    val ultimaBoleta = data?.ultimaBoleta
    val puntosVerdes = data?.puntosVerdes ?: 0
    val co2Acumulado = data?.co2Acumulado ?: 0.0
    val promociones = data?.promociones ?: emptyList()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
        ) {
            // Card de Último Recibo con datos del endpoint
            if (ultimaBoleta != null) {
                LastReceiptCard(lastReceipt = ultimaBoleta)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de Estadísticas con datos del endpoint
            StatsSection(
                puntosVerdes = puntosVerdes,
                co2Acumulado = co2Acumulado,
                onVerPromosClick = onVerPromosClick
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Sección de Ofertas con datos del endpoint
            OffersSection(promociones = promociones)

            Spacer(modifier = Modifier.height(32.dp))

            // Botones de acción
            ActionButtonsSection(
                onEscanearClick = onEscanearClick,
                onVerImpactoClick = onVerImpactoClick
            )

            Spacer(modifier = Modifier.height(32.dp))
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

