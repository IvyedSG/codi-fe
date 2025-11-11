package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.theme.CodiThemeValues

@Composable
fun ActionButtonsSection(
    onEscanearClick: () -> Unit,
    onVerImpactoClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Botón Escanear
        Button(
            onClick = onEscanearClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = CodiThemeValues.colorScheme.secondary,
                contentColor = CodiThemeValues.colorScheme.primary
            ),
            shape = CodiThemeValues.shapes.medium,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Escanear",
                tint = LocalContentColor.current,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Escanear",
                style = CodiThemeValues.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LocalContentColor.current
            )
        }

        // Botón Ver Impacto
        Button(
            onClick = onVerImpactoClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = CodiThemeValues.colorScheme.tertiary,
                contentColor = CodiThemeValues.colorScheme.onTertiary
            ),
            shape = CodiThemeValues.shapes.medium,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.BarChart,
                contentDescription = "Ver Impacto",
                tint = LocalContentColor.current,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ver Impacto",
                style = CodiThemeValues.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LocalContentColor.current
            )
        }
    }
}

