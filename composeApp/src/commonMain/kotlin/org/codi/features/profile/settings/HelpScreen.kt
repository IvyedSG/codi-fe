package org.codi.features.profile.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.theme.CodiThemeValues

object HelpScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val scrollState = rememberScrollState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Ayuda",
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
                    .padding(20.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Botón de contactar soporte
                Button(
                    onClick = {
                        // TODO: Abrir email o chat de soporte
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodiThemeValues.colorScheme.primary,
                        contentColor = CodiThemeValues.colorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Contactar Soporte",
                        style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Preguntas Frecuentes",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )

                // FAQ 1
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "¿Cómo escaneo un recibo?",
                            style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onSurface
                        )
                        Text(
                            text = "Ve a la pestaña 'Subir', toca el botón de la cámara y captura tu recibo. La app lo procesará automáticamente.",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // FAQ 2
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "¿Qué son los puntos verdes?",
                            style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onSurface
                        )
                        Text(
                            text = "Los puntos verdes son recibos digitalizados. Los acumulas al escanear tus recibos y puedes canjearlos por promociones exclusivas.",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // FAQ 3
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "¿Cómo canjeo una promoción?",
                            style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onSurface
                        )
                        Text(
                            text = "Ve a la pestaña 'Promos', selecciona la promoción que deseas y toca 'Canjear' si cumples con los requisitos de puntos verdes.",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                // FAQ 4
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "¿Qué es el CO₂ acumulado?",
                            style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onSurface
                        )
                        Text(
                            text = "Es una estimación de la huella de carbono generada por tus compras. Te ayuda a tomar decisiones más conscientes sobre tu consumo.",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¿No encuentras lo que buscas? Contáctanos y te ayudaremos.",
                    style = CodiThemeValues.typography.bodySmall,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

