package org.codi.features.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.theme.CodiThemeValues

object NotificationsScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var receivePromotions by remember { mutableStateOf(true) }
        var receiveUpdates by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Notificaciones",
                            style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = CodiThemeValues.colorScheme.background,
                        titleContentColor = CodiThemeValues.colorScheme.onBackground,
                        navigationIconContentColor = CodiThemeValues.colorScheme.onBackground
                    )
                )
            },
            containerColor = CodiThemeValues.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Configuración de Notificaciones",
                    style = CodiThemeValues.typography.bodyLarge,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Toggle para promociones
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CodiThemeValues.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Recibir Promociones",
                                style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = CodiThemeValues.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Recibe notificaciones de nuevas promociones y ofertas",
                                style = CodiThemeValues.typography.bodySmall,
                                color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Switch(
                            checked = receivePromotions,
                            onCheckedChange = { receivePromotions = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CodiThemeValues.colorScheme.primary,
                                checkedTrackColor = CodiThemeValues.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        )
                    }
                }

                // Toggle para actualizaciones
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CodiThemeValues.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Actualizaciones de la App",
                                style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = CodiThemeValues.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Notificaciones sobre nuevas funciones y mejoras",
                                style = CodiThemeValues.typography.bodySmall,
                                color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Switch(
                            checked = receiveUpdates,
                            onCheckedChange = { receiveUpdates = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CodiThemeValues.colorScheme.primary,
                                checkedTrackColor = CodiThemeValues.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
        }
    }
}

