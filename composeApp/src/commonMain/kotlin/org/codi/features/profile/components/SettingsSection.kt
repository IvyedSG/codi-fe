package org.codi.features.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.features.profile.settings.AboutScreen
import org.codi.features.profile.settings.HelpScreen
import org.codi.features.profile.settings.NotificationsScreen
import org.codi.features.profile.settings.PrivacyScreen
import org.codi.theme.CodiThemeValues

@Composable
fun SettingsSection() {
    val navigator = LocalNavigator.currentOrThrow

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            SettingsItem(Icons.Default.Notifications, "Notificaciones") {
                navigator.push(NotificationsScreen)
            }
            DividerItem()
            SettingsItem(Icons.Default.Lock, "Privacidad") {
                navigator.push(PrivacyScreen)
            }
            DividerItem()
            SettingsItem(Icons.Default.Info, "Acerca de") {
                navigator.push(AboutScreen)
            }
            DividerItem()
            SettingsItem(Icons.AutoMirrored.Filled.Help, "Ayuda") {
                navigator.push(HelpScreen)
            }
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
