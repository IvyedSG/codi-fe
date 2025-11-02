package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GreenReceiptsColumn(
            modifier = Modifier.weight(1f),
            count = "3"
        )

        CO2AccumulatedColumn(
            modifier = Modifier.weight(1f),
            amount = "10 Kg"
        )
    }
}

