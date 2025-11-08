package org.codi.features.upload

import androidx.compose.runtime.Composable

@Composable
actual fun rememberCameraLauncher(onImageCaptured: (ByteArray) -> Unit): () -> Unit {
    // Por ahora simulamos la captura en iOS
    // En producción usarías UIImagePickerController
    return {
        // Simulación: crear un ByteArray vacío de prueba
        // En producción, aquí convertirías UIImage a ByteArray
        val dummyBytes = ByteArray(100)
        onImageCaptured(dummyBytes)
    }
}

@Composable
actual fun rememberGalleryLauncher(onImageSelected: (ByteArray) -> Unit): () -> Unit {
    // Por ahora simulamos la selección en iOS
    // En producción usarías UIImagePickerController con sourceType = .photoLibrary
    return {
        // Simulación: crear un ByteArray vacío de prueba
        // En producción, aquí convertirías UIImage a ByteArray
        val dummyBytes = ByteArray(100)
        onImageSelected(dummyBytes)
    }
}

