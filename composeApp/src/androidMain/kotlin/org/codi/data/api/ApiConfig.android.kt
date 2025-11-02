package org.codi.data.api

import android.os.Build

/**
 * Configuración de la URL base de la API para Android.
 * Detecta si la app corre en emulador o en un dispositivo físico.
 */
actual val BASE_API_URL: String = if (Build.FINGERPRINT.contains("generic")) {
    // 🧩 Emulador Android
    "http://10.0.2.2:8000"
} else {
    // 📱 Dispositivo físico Android - usa la IP local de tu PC
    "http://192.168.1.33:8000"
}
