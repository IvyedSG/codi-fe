package org.codi.utils

import androidx.compose.ui.graphics.Color
import org.codi.data.models.BoletaTipoAmbiental
import org.codi.theme.PrimaryGreen

/**
 * Utilidades para trabajar con tipos ambientales
 */
object TipoAmbientalUtils {

    /**
     * Obtiene el color del badge según el tipo ambiental
     */
    fun getColor(tipo: BoletaTipoAmbiental): Color {
        return when (tipo) {
            BoletaTipoAmbiental.VERDE -> PrimaryGreen
            BoletaTipoAmbiental.AMARILLO -> Color(0xFFFF9800) // Naranja/Amarillo
            BoletaTipoAmbiental.ROJO -> Color(0xFFF44336) // Rojo
        }
    }

    /**
     * Obtiene el color del badge desde un string
     */
    fun getColorFromString(tipoString: String?): Color {
        val tipo = BoletaTipoAmbiental.fromString(tipoString)
        return getColor(tipo)
    }

    /**
     * Obtiene el nombre formateado del tipo ambiental
     */
    fun getDisplayName(tipo: BoletaTipoAmbiental): String {
        return when (tipo) {
            BoletaTipoAmbiental.VERDE -> "Verde"
            BoletaTipoAmbiental.AMARILLO -> "Amarillo"
            BoletaTipoAmbiental.ROJO -> "Rojo"
        }
    }

    /**
     * Obtiene un emoji representativo del tipo ambiental
     */
    fun getEmoji(tipo: BoletaTipoAmbiental): String {
        return when (tipo) {
            BoletaTipoAmbiental.VERDE -> "🌱"
            BoletaTipoAmbiental.AMARILLO -> "⚠️"
            BoletaTipoAmbiental.ROJO -> "⚠️"
        }
    }
}

