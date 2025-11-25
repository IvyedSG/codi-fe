package org.codi.features.upload

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.codi.features.receipt.ReceiptDetailScreen
import org.codi.features.receipt.ReceiptViewModel
import org.codi.features.receipt.UploadState
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.theme.SecondaryGreen

// Funciones expect para lanzar la cámara y galería
@Composable
expect fun rememberCameraLauncher(onImageCaptured: (ByteArray) -> Unit): () -> Unit

@Composable
expect fun rememberGalleryLauncher(onImageSelected: (ByteArray) -> Unit): () -> Unit

object UploadTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.CameraAlt)
            return remember {
                TabOptions(
                    index = 2u,
                    title = "Subir",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(UploadTabScreen())
    }
}

class UploadTabScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = remember { ReceiptViewModel() }
        val uploadState = viewModel.uploadState

        // Manejar cambios de estado del ViewModel
        LaunchedEffect(uploadState) {
            when (uploadState) {
                is UploadState.Success -> {
                    val boletaId = uploadState.response.data?.boletaId
                    if (boletaId != null) {
                        navigator.push(ReceiptDetailScreen(
                            receiptId = boletaId,
                            storeName = "TOTTUS",
                            fromUpload = true
                        ))
                        // Resetear el estado para la próxima captura
                        viewModel.resetStates()
                    }
                }
                is UploadState.Error -> {
                    // se mostrará en la UI
                }
                else -> {}
            }
        }

        when (uploadState) {
            is UploadState.Idle -> {
                UploadCaptureScreen(
                    onImageCaptured = { imageBytes ->
                        viewModel.uploadReceipt(imageBytes)
                    }
                )
            }
            is UploadState.Uploading -> {
                ProcessingScreen()
            }
            is UploadState.Error -> {
                ErrorScreen(
                    message = uploadState.message,
                    onRetry = { viewModel.resetStates() }
                )
            }
            else -> {
                // Otros estados se manejan con LaunchedEffect
                ProcessingScreen()
            }
        }
    }
}

@Composable
fun ProcessingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                color = PrimaryGreen,
                strokeWidth = 4.dp
            )
            Text(
                text = "Analizando recibo...",
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )
            Text(
                text = "Calculando huella de carbono",
                style = CodiThemeValues.typography.bodyMedium,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        org.codi.common.components.TopBar(
            title = "Error"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = Color(0xFFE53935),
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Error al procesar",
                style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = message,
                style = CodiThemeValues.typography.bodyMedium,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CodiThemeValues.colorScheme.tertiary,
                    contentColor = CodiThemeValues.colorScheme.onTertiary
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Intentar de nuevo",
                    style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = LocalContentColor.current
                )
            }
        }
    }
}

@Composable
fun UploadCaptureScreen(onImageCaptured: (ByteArray) -> Unit) {
    // Launchers para cámara y galería (multiplataforma)
    val launchCamera = rememberCameraLauncher(onImageCaptured = onImageCaptured)
    val launchGallery = rememberGalleryLauncher(onImageSelected = onImageCaptured)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        // TopBar fijo
        org.codi.common.components.TopBar(
            title = "Subir Factura"
        )

        // Contenido scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Título
            Text(
                text = "Toma una foto de tu factura",
                style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = "Captura la imagen de tu boleta para calcular la huella de carbono",
                style = CodiThemeValues.typography.bodyMedium,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Área de cámara
            CameraAreaPlaceholder()

            Spacer(modifier = Modifier.height(40.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón Galería
                OutlinedButton(
                    onClick = launchGallery,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = PrimaryGreen
                    ),
                    border = BorderStroke(2.dp, PrimaryGreen),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Galería",
                        style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                }

                // Botón Capturar - Abre la cámara real
                Button(
                    onClick = launchCamera,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CodiThemeValues.colorScheme.tertiary,
                        contentColor = CodiThemeValues.colorScheme.onTertiary
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Capturar",
                        style = CodiThemeValues.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = LocalContentColor.current
                    )
                }
            }
        }
    }
}


@Composable
fun CameraAreaPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Icono de cámara
            Surface(
                color = SecondaryGreen.copy(alpha = 0.3f),
                shape = CircleShape,
                modifier = Modifier.size(80.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = PrimaryGreen,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // Texto
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Alinea tu recibo",
                    style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Text(
                    text = "Posiciona la factura dentro del marco",
                    style = CodiThemeValues.typography.bodySmall,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
