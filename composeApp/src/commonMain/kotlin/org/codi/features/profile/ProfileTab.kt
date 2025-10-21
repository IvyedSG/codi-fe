package org.codi.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            return remember {
                TabOptions(
                    index = 4u,
                    title = "Perfil",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        ProfileScreen()
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        // TopBar
        org.codi.common.components.TopBar(
            title = "Perfil"
        )

        // Contenido scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Card de información del usuario
            UserInfoCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas personales
            PersonalStatsCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Opciones de configuración
            Text(
                text = "Configuración",
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingsSection()

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de cerrar sesión
            Button(
                onClick = { /* TODO: Cerrar sesión */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                ),
                shape = CodiThemeValues.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = LocalContentColor.current
                )
            }
        }
    }
}

@Composable
fun UserInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(70.dp),
                shape = CircleShape,
                color = PrimaryGreen.copy(alpha = 0.15f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre
            Text(
                text = "Juan Pérez",
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Email
            Text(
                text = "juan.perez@email.com",
                style = CodiThemeValues.typography.bodySmall,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón editar perfil
            OutlinedButton(
                onClick = { /* TODO: Editar perfil */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryGreen),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PrimaryGreen
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Editar Perfil",
                    style = CodiThemeValues.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Composable
fun PersonalStatsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Mis Estadísticas",
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.Receipt,
                    value = "12",
                    label = "Recibos"
                )

                StatItem(
                    icon = Icons.Default.Eco,
                    value = "8",
                    label = "Eco"
                )

                StatItem(
                    icon = Icons.Default.Cloud,
                    value = "48 Kg",
                    label = "CO2"
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = value,
            style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )

        Text(
            text = label,
            style = CodiThemeValues.typography.bodySmall,
            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun SettingsSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "Notificaciones",
                onClick = { /* TODO */ }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.1f)
            )

            SettingsItem(
                icon = Icons.Default.Lock,
                title = "Privacidad",
                onClick = { /* TODO */ }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.1f)
            )

            SettingsItem(
                icon = Icons.Default.Info,
                title = "Acerca de",
                onClick = { /* TODO */ }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.1f)
            )

            SettingsItem(
                icon = Icons.Default.Help,
                title = "Ayuda",
                onClick = { /* TODO */ }
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = title,
                style = CodiThemeValues.typography.bodyMedium,
                color = CodiThemeValues.colorScheme.onBackground
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp)
        )
    }
}

