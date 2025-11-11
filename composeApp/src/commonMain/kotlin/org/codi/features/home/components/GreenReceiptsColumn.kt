package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.codi.theme.CodiThemeValues

@Composable
fun GreenReceiptsColumn(
    modifier: Modifier = Modifier,
    count: String,
    onVerPromosClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Recibos Verdes",
            style = CodiThemeValues.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = CodiThemeValues.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = count,
            style = CodiThemeValues.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp
            ),
            color = CodiThemeValues.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onVerPromosClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = CodiThemeValues.colorScheme.tertiary,
                contentColor = CodiThemeValues.colorScheme.onTertiary
            ),
            shape = CodiThemeValues.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ver Promos",
                style = CodiThemeValues.typography.labelMedium,
                color = LocalContentColor.current
            )
        }
    }
}

