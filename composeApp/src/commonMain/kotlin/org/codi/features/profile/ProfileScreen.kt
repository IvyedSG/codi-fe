package org.codi.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.codi.common.components.TopBar
import org.codi.features.auth.LoginScreen
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.features.profile.components.ProfileContent
import org.codi.features.profile.components.EditProfileDialog

@Composable
fun ProfileScreen() {
    val viewModel = remember { ProfileViewModel() }
    // Acceder al Navigator de la tab (para navegación dentro de Profile)
    val navigator = LocalNavigator.currentOrThrow

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        TopBar(title = "Perfil")

        when (val currentState = viewModel.state) {
            is ProfileState.Loading -> LoadingState()
            is ProfileState.Error -> ErrorState(
                message = currentState.message,
                onRetry = { viewModel.loadProfile() }
            )
            is ProfileState.Success -> {
                ProfileContent(
                    profileData = currentState.profile,
                    viewModel = viewModel,
                    onLogout = {
                        viewModel.logout()
                        // Para el logout, necesitamos acceder al Navigator principal
                        // Buscar el navigator raíz para volver al login
                        var rootNavigator = navigator
                        while (rootNavigator.parent != null) {
                            rootNavigator = rootNavigator.parent!!
                        }
                        rootNavigator.replaceAll(LoginScreen)
                    }
                )
            }
        }
    }

    // Diálogo de edición de perfil
    EditProfileDialog(viewModel = viewModel)
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
