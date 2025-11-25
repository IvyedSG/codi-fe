package org.codi.data.api

import android.os.Build

/**
 * Configuración de la URL base de la API para Android.
 * Detecta si la app corre en emulador o en un dispositivo físico.
 */
actual val BASE_API_URL: String = if (Build.FINGERPRINT.contains("generic")) {
    // 🧩 Emulador Android (apunta al localhost del PC)
    "http://10.0.2.2:8000"
} else {
    // 📱 Dispositivo físico / release: usar dominio público
    "https://codi-be-7fdq.onrender.com"
}