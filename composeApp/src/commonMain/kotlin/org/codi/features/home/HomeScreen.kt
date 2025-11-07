package org.codi.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codi.common.components.TopBar
import org.codi.features.home.components.EmptyStateGuide
import org.codi.features.home.components.HomeContent
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.ui.ViewModelStore

@Composable
fun HomeScreen() {
    val viewModel = ViewModelStore.getHomeViewModel()

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        TopBar(title = "Bienvenido a Codi")

        when (val currentState = viewModel.state) {
            is HomeState.Loading -> LoadingState()
            is HomeState.Error -> ErrorState(
                message = currentState.message,
                onRetry = { viewModel.loadHomeData() }
            )
            is HomeState.Success -> {
                // Mostrar EmptyState si el usuario no tiene recibos
                if (currentState.isEmpty) {
                    EmptyStateGuide()
                } else {
                    HomeContent(homeData = currentState.home)
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
