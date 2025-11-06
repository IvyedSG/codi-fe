package org.codi.features.profile.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.theme.CodiThemeValues

object PrivacyScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Privacidad",
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
                    text = "Políticas y Términos",
                    style = CodiThemeValues.typography.bodyLarge,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Política de Privacidad
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // TODO: Abrir navegador con la URL de política de privacidad
                        },
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
                                text = "Política de Privacidad",
                                style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = CodiThemeValues.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Lee cómo protegemos tus datos",
                                style = CodiThemeValues.typography.bodySmall,
                                color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "Abrir",
                            tint = CodiThemeValues.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Términos y Condiciones
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // TODO: Abrir navegador con la URL de términos y condiciones
                        },
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
                                text = "Términos y Condiciones",
                                style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                color = CodiThemeValues.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Conoce los términos de uso de la aplicación",
                                style = CodiThemeValues.typography.bodySmall,
                                color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                            contentDescription = "Abrir",
                            tint = CodiThemeValues.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Al usar Codi, aceptas nuestras políticas de privacidad y términos de uso.",
                    style = CodiThemeValues.typography.bodySmall,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

