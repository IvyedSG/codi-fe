package org.codi.features.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.theme.CodiThemeValues

object AboutScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Acerca de",
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Logo de la app
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = "Codi Logo",
                    tint = CodiThemeValues.colorScheme.primary,
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = "Codi",
                    style = CodiThemeValues.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.primary
                )

                Text(
                    text = "Versión 1.0.0",
                    style = CodiThemeValues.typography.bodyMedium,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Misión de la app
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = CodiThemeValues.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Nuestra Misión",
                            style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.primary
                        )
                        Text(
                            text = "Promover el consumo consciente y sostenible a través de la digitalización de recibos, ayudando a reducir la huella de carbono y fomentando hábitos de compra más ecológicos.",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onSurface,
                            textAlign = TextAlign.Justify,
                            lineHeight = CodiThemeValues.typography.bodyMedium.lineHeight
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Información adicional
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Desarrollado con 💚",
                        style = CodiThemeValues.typography.bodySmall,
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "© 2025 Codi. Todos los derechos reservados.",
                        style = CodiThemeValues.typography.bodySmall,
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

