package org.codi.common.camera

import androidx.compose.runtime.Composable

expect class CameraCapture() {
    @Composable
    fun CaptureImage(
        onImageCaptured: (ByteArray) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit
    )
}

expect fun launchCamera(
    onImageCaptured: (ByteArray) -> Unit,
    onError: (Exception) -> Unit
)

expect fun launchGallery(
    onImageSelected: (ByteArray) -> Unit,
    onError: (Exception) -> Unit
)

