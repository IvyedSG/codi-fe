package org.codi.features.promos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.codi.features.promos.components.FeaturedPromoBanner
import org.codi.features.promos.components.PromocionItem
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

object PromosTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Percent)

            return remember {
                TabOptions(
                    index = 1u,
                    title = "Promos",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val viewModel = remember { PromoViewModel() }
        PromosScreen(viewModel)
    }
}

@Composable
fun PromosScreen(viewModel: PromoViewModel) {
    val state = viewModel.state

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodiThemeValues.colorScheme.background)
        ) {
            // TopBar fijo - no hace scroll
            org.codi.common.components.TopBar(
                title = "Promociones",
                showGreenReceipts = true,
                greenReceiptsCount = state.puntosUsuario
            )

            // Contenido scrollable
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // Banner principal
                item {
                    FeaturedPromoBanner()
                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Título de promociones disponibles
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Eco,
                            contentDescription = null,
                            tint = SecondaryGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Promociones disponibles",
                            style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Lista de promociones del API
                if (state.promociones.isEmpty() && !state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay promociones disponibles",
                                style = CodiThemeValues.typography.bodyLarge,
                                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                items(state.promociones) { promocion ->
                    PromocionItem(
                        promocion = promocion,
                        puntosUsuario = state.puntosUsuario,
                        onCanjear = { promocionId ->
                            viewModel.canjearPromocion(
                                promocionId = promocionId,
                                descripcion = "Canje desde la aplicación"
                            )
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Loading overlay
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(CodiThemeValues.colorScheme.background.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SecondaryGreen)
            }
        }

        // Error message
        state.error?.let { errorMessage ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK", color = SecondaryGreen)
                    }
                }
            ) {
                Text(errorMessage)
            }
        }
    }
}
