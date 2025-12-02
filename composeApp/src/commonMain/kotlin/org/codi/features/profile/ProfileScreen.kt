package org.codi.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.codi.common.components.TopBar
import org.codi.features.auth.LoginScreen
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.features.profile.components.ProfileContent
import org.codi.features.profile.components.EditProfileDialog
import org.codi.ui.ViewModelStore

@Composable
fun ProfileScreen() {
    val viewModel = ViewModelStore.getProfileViewModel()
    // Acceder al Navigator de la tab (para navegación dentro de Profile)
    val navigator = LocalNavigator.currentOrThrow
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
        viewModel.loadProfile()
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
                                        data.visuals.message.contains("correctamente", ignoreCase = true)) {
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
                            coroutineScope.launch {
                                viewModel.logout()
                                // Limpiar todos los ViewModels al cerrar sesión
                                ViewModelStore.clear()
                                // Para el logout, necesitamos acceder al Navigator principal
                                // Buscar el navigator raíz para volver al login
                                var rootNavigator = navigator
                                while (rootNavigator.parent != null) {
                                    rootNavigator = rootNavigator.parent!!
                                }
                                rootNavigator.replaceAll(LoginScreen)
                            }
                        }
                    )
                }
            }
        }

        // Diálogo de edición de perfil
        EditProfileDialog(viewModel = viewModel)
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
