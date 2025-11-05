package org.codi.features.promos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.codi.features.promos.components.PromocionItem
import org.codi.features.promos.components.RedeemedPromoItem
import org.codi.features.promos.components.PromoDetailDialog
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
    var selectedPromocionId by remember { mutableStateOf<String?>(null) }

    // Auto-ocultar el error después de 5 segundos
    LaunchedEffect(state.error) {
        if (state.error != null) {
            kotlinx.coroutines.delay(5000)
            viewModel.clearError()
        }
    }

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

            // TabRow para cambiar entre Disponibles y Mis Canjes
            TabRow(
                selectedTabIndex = viewModel.currentTab,
                containerColor = CodiThemeValues.colorScheme.background,
                contentColor = SecondaryGreen,
                indicator = { tabPositions ->
                    if (viewModel.currentTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[viewModel.currentTab]),
                            color = SecondaryGreen,
                            height = 3.dp
                        )
                    }
                }
            ) {
                Tab(
                    selected = viewModel.currentTab == 0,
                    onClick = { viewModel.selectTab(0) },
                    text = {
                        Text(
                            text = "Disponibles",
                            style = CodiThemeValues.typography.titleSmall.copy(
                                fontWeight = if (viewModel.currentTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    selectedContentColor = SecondaryGreen,
                    unselectedContentColor = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                )
                Tab(
                    selected = viewModel.currentTab == 1,
                    onClick = { viewModel.selectTab(1) },
                    text = {
                        Text(
                            text = "Mis Canjes",
                            style = CodiThemeValues.typography.titleSmall.copy(
                                fontWeight = if (viewModel.currentTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    selectedContentColor = SecondaryGreen,
                    unselectedContentColor = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            // Contenido según la pestaña seleccionada
            when (viewModel.currentTab) {
                0 -> PromocionesDisponiblesContent(viewModel, state)
                1 -> PromocionesCanjeadasContent(
                    state = state,
                    onVerDetalle = { promocionId ->
                        selectedPromocionId = promocionId
                    }
                )
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
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK", color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            ) {
                Text(errorMessage)
            }
        }
    }

    // Diálogo de detalle de promoción
    selectedPromocionId?.let { promocionId ->
        PromoDetailDialog(
            promocionId = promocionId,
            onDismiss = { selectedPromocionId = null }
        )
    }
}

@Composable
private fun PromocionesDisponiblesContent(
    viewModel: PromoViewModel,
    state: PromoState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Espacio superior
        item {
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

        // Lista de promociones disponibles
        if (state.promocionesDisponibles.isEmpty() && !state.isLoading) {
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

        items(state.promocionesDisponibles) { promocion ->
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

@Composable
private fun PromocionesCanjeadasContent(
    state: PromoState,
    onVerDetalle: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Título de mis canjes
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SecondaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Mis promociones canjeadas",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Lista de promociones canjeadas
        if (state.promocionesCanjeadas.isEmpty() && !state.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Redeem,
                            contentDescription = null,
                            tint = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "Aún no has canjeado promociones",
                            style = CodiThemeValues.typography.bodyLarge,
                            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        items(state.promocionesCanjeadas) { promocion ->
            RedeemedPromoItem(
                promocion = promocion,
                onVerDetalle = onVerDetalle
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
