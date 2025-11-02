package org.codi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.codi.App
import org.codi.AppContextHolder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar AppContextHolder para TokenStorage
        AppContextHolder.init(this)

        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}
