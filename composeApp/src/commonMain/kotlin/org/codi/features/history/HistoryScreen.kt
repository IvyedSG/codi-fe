package org.codi.features.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codi.common.components.TopBar
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.features.history.components.HistoryContent

@Composable
fun HistoryScreen() {
    val viewModel = remember { HistoryViewModel() }

    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        TopBar(title = "Historial")

        when (val currentState = viewModel.state) {
            is HistoryState.Loading -> LoadingState()
            is HistoryState.Error -> ErrorState(
                message = currentState.message,
                onRetry = { viewModel.loadHistory() }
            )
            is HistoryState.Success -> {
                HistoryContent(historyData = currentState.history)
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

