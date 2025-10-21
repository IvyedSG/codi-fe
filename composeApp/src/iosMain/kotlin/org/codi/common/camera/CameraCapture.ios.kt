package org.codi.common.camera

import androidx.compose.runtime.Composable

actual class CameraCapture {
    @Composable
    actual fun CaptureImage(
        onImageCaptured: (ByteArray) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit
    ) {
        // Implementación básica para iOS - simulación
        // En producción usarías UIImagePickerController
    }
}

actual fun launchCamera(
    onImageCaptured: (ByteArray) -> Unit,
    onError: (Exception) -> Unit
) {
    // Implementación básica para iOS
}

actual fun launchGallery(
    onImageSelected: (ByteArray) -> Unit,
    onError: (Exception) -> Unit
) {
    // Implementación básica para iOS
}

