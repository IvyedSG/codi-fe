package org.codi.data.api

import android.os.Build

/**
 * Configuración de la URL base de la API para Android.
 * Detecta si la app corre en emulador o en un dispositivo físico.
 */
actual val BASE_API_URL: String = if (Build.FINGERPRINT.contains("generic")) {
    // 🧩 Emulador Android
    "http://72.61.45.31"
} else {
    // 📱 Dispositivo físico
    "http://72.61.45.31"
}