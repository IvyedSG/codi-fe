package org.codi.common.camera

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

actual class CameraCapture {
    @Composable
    actual fun CaptureImage(
        onImageCaptured: (ByteArray) -> Unit,
        onError: (Exception) -> Unit,
        onDismiss: () -> Unit
    ) {
        val context = LocalContext.current
        val file = File(context.cacheDir, "captured_image_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->
            if (success) {
                try {
                    val bytes = file.readBytes()
                    onImageCaptured(bytes)
                } catch (e: Exception) {
                    onError(e)
                }
            } else {
                onDismiss()
            }
        }

        launcher.launch(uri)
    }
}

actual fun launchCamera(
    onImageCaptured: (ByteArray) -> Unit,
    onError: (Exception) -> Unit
) {
    // Implementación básica para Android
}

actual fun launchGallery(
    onImageSelected: (ByteArray) -> Unit,
    onError: (Exception) -> Unit
) {
    // Implementación básica para Android
}

