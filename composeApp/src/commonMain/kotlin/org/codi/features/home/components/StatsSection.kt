package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatsSection(
    puntosVerdes: Int,
    co2Acumulado: Double,
    onVerPromosClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GreenReceiptsColumn(
            modifier = Modifier.weight(1f),
            count = puntosVerdes.toString(),
            onVerPromosClick = onVerPromosClick
        )

        CO2AccumulatedColumn(
            modifier = Modifier.weight(1f),
            amount = "${(co2Acumulado * 10).toInt() / 10.0} Kg"
        )
    }
}

