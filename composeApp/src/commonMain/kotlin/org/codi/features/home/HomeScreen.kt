package org.codi.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import kotlinx.coroutines.delay
import org.codi.common.components.TopBar
import org.codi.features.home.components.EmptyStateGuide
import org.codi.features.home.components.HomeContent
import org.codi.features.profile.ProfileTab
import org.codi.features.promos.PromosTab
import org.codi.features.upload.UploadTab
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.ui.ViewModelStore

@Composable
fun HomeScreen() {
    val viewModel = ViewModelStore.getHomeViewModel()
    val tabNavigator = LocalTabNavigator.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Observar mensajes de éxito
    LaunchedEffect(viewModel.successMessage.value) {
        viewModel.successMessage.value?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            delay(100)
            viewModel.clearMessages()
        }
    }

    // Observar mensajes de error
    LaunchedEffect(viewModel.errorMessage.value) {
        viewModel.errorMessage.value?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
            delay(100)
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (data.visuals.message.contains("✓") ||
                                        data.visuals.message.contains("actualizado", ignoreCase = true)) {
                        Color(0xFF4CAF50) // Verde para éxito
                    } else {
                        MaterialTheme.colorScheme.errorContainer // Rojo para error
                    },
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodiThemeValues.colorScheme.background)
                .padding(paddingValues)
        ) {
            TopBar(title = "Bienvenido a Codi")

            when (val currentState = viewModel.state) {
                is HomeState.Loading -> LoadingState()
                is HomeState.Error -> ErrorState(
                    message = currentState.message,
                    onRetry = { viewModel.loadHomeData(forceRefresh = true) }
                )
                is HomeState.Success -> {
                    // Mostrar EmptyState si el usuario no tiene recibos
                    if (currentState.isEmpty) {
                        EmptyStateGuide(
                            onEscanearClick = {
                                // Navegar a la pestaña de Upload (Subir)
                                tabNavigator.current = UploadTab
                            }
                        )
                    } else {
                        HomeContent(
                            homeData = currentState.home,
                            isRefreshing = currentState.isRefreshing,
                            onEscanearClick = {
                                // Navegar a la pestaña de Upload (Subir)
                                tabNavigator.current = UploadTab
                            },
                            onVerImpactoClick = {
                                // Navegar a la pestaña de Profile
                                tabNavigator.current = ProfileTab
                            },
                            onVerPromosClick = {
                                // Navegar a la pestaña de Promos
                                tabNavigator.current = PromosTab
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = PrimaryGreen)
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = CodiThemeValues.typography.bodyMedium
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Reintentar")
            }
        }
    }
}
