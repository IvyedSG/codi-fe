package org.codi.features.upload

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun rememberCameraLauncher(onImageCaptured: () -> Unit): () -> Unit {
    val context = LocalContext.current

    // Launcher para capturar foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            // Imagen capturada exitosamente
            onImageCaptured()
        }
    }

    // Launcher para solicitar permiso de cámara
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Permiso otorgado, abrir cámara inmediatamente
            cameraLauncher.launch(null)
        }
    }

    return {
        // Verificar si ya tenemos el permiso
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            // Ya tenemos permiso, abrir cámara
            cameraLauncher.launch(null)
        } else {
            // Solicitar permiso (después abrirá la cámara automáticamente)
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}

@Composable
actual fun rememberGalleryLauncher(onImageSelected: () -> Unit): () -> Unit {
    // Launcher para seleccionar imagen de la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            // Imagen seleccionada exitosamente
            onImageSelected()
        }
    }

    return {
        // Abrir galería para seleccionar imagen
        galleryLauncher.launch("image/*")
    }
}

