package org.codi.utils

/**
 * Formatea un número Double a String con el número especificado de decimales
 */
fun Double.formatDecimal(decimals: Int = 2): String {
    val multiplier = when (decimals) {
        0 -> 1
        1 -> 10
        2 -> 100
        3 -> 1000
        else -> 100
    }
    val rounded = kotlin.math.round(this * multiplier) / multiplier

    // Convertir a string y asegurar que tenga los decimales correctos
    val parts = rounded.toString().split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1] else ""

    // Agregar ceros faltantes
    val paddedDecimal = decimalPart.padEnd(decimals, '0').take(decimals)

    return if (decimals > 0) {
        "$integerPart.$paddedDecimal"
    } else {
        integerPart
    }
}

