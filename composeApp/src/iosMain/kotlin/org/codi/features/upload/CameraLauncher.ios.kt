package org.codi.features.upload

import androidx.compose.runtime.Composable

@Composable
actual fun rememberCameraLauncher(onImageCaptured: () -> Unit): () -> Unit {
    // Por ahora simulamos la captura en iOS
    // En producción usarías UIImagePickerController
    return {
        // Simulación: llamar directamente al callback
        onImageCaptured()
    }
}

@Composable
actual fun rememberGalleryLauncher(onImageSelected: () -> Unit): () -> Unit {
    // Por ahora simulamos la selección en iOS
    // En producción usarías UIImagePickerController con sourceType = .photoLibrary
    return {
        // Simulación: llamar directamente al callback
        onImageSelected()
    }
}

