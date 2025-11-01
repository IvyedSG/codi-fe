package org.codi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.codi.App
import org.codi.AppContextHolder
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {

    companion object {
        // Google Web Client ID para autenticación
        const val GOOGLE_WEB_CLIENT_ID = "275267069227-vlak55jq1fht1t7cuc90mad9j591bs7f.apps.googleusercontent.com"

        // Referencia débil a la actividad actual (para Google Sign-In)
        var currentActivity: WeakReference<ComponentActivity>? = null
            private set
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Guardar referencia a esta actividad
        currentActivity = WeakReference(this)

        // Guardar el contexto de la aplicación
        AppContextHolder.setContext(applicationContext)

        enableEdgeToEdge()
        setContent {
            App()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar la referencia si es esta actividad
        if (currentActivity?.get() == this) {
            currentActivity = null
        }
    }
}

