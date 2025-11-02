package org.codi.features.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

@Composable
fun CO2AccumulatedColumn(
    modifier: Modifier = Modifier,
    amount: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CO₂ acumulado",
            style = CodiThemeValues.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = CodiThemeValues.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = amount,
            style = CodiThemeValues.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp
            ),
            color = CodiThemeValues.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Indicador verde
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(SecondaryGreen)
        )
    }
}

