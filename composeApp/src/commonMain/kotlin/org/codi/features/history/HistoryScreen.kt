package org.codi.features.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.codi.common.components.TopBar
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.features.history.components.HistoryContent
import org.codi.ui.ViewModelStore

@Composable
fun HistoryScreen() {
    val viewModel = ViewModelStore.getHistoryViewModel()
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
        viewModel.loadHistory()
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
            TopBar(title = "Historial")

            when (val currentState = viewModel.state) {
                is HistoryState.Loading -> LoadingState()
                is HistoryState.Error -> ErrorState(
                    message = currentState.message,
                    onRetry = { viewModel.loadHistory(forceRefresh = true) }
                )
                is HistoryState.Success -> {
                    HistoryContent(
                        historyData = currentState.history,
                        isRefreshing = currentState.isRefreshing
                    )
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

