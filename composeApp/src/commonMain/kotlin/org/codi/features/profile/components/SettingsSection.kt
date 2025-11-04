package org.codi.features.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.codi.theme.CodiThemeValues

@Composable
fun SettingsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsItem(Icons.Default.Notifications, "Notificaciones") { /* TODO */ }
            DividerItem()
            SettingsItem(Icons.Default.Lock, "Privacidad") { /* TODO */ }
            DividerItem()
            SettingsItem(Icons.Default.Info, "Acerca de") { /* TODO */ }
            DividerItem()
            SettingsItem(Icons.Default.Help, "Ayuda") { /* TODO */ }
        }
    }
}

@Composable
private fun DividerItem() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.1f)
    )
}
