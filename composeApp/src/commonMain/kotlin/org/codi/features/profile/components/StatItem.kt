package org.codi.features.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

@Composable
fun StatItem(icon: ImageVector, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = value,
            style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )
        Text(
            text = label,
            style = CodiThemeValues.typography.labelSmall,
            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}
