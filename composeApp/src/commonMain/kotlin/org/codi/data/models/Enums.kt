package org.codi.data.models

/**
 * Enums para la aplicación Codi
 */

/**
 * Tipo ambiental de una boleta según su impacto ecológico
 */
enum class BoletaTipoAmbiental {
    VERDE,
    AMARILLO,
    ROJO;

    companion object {
        /**
         * Convierte un string a BoletaTipoAmbiental
         * @param value String que representa el tipo ("VERDE", "AMARILLO", "ROJO")
         * @return BoletaTipoAmbiental correspondiente, o ROJO por defecto si no coincide
         */
        fun fromString(value: String?): BoletaTipoAmbiental {
            return when (value?.uppercase()) {
                "VERDE" -> VERDE
                "AMARILLO", "AMARILLA" -> AMARILLO
                "ROJO", "ROJA" -> ROJO
                else -> ROJO // Por defecto ROJO si no se reconoce
            }
        }
    }
}

/**
 * Tipo de moneda
 */
enum class MonedaTipo {
    PEN,
    USD;

    companion object {
        /**
         * Convierte un string a MonedaTipo
         * @param value String que representa la moneda ("PEN", "USD")
         * @return MonedaTipo correspondiente, o PEN por defecto
         */
        fun fromString(value: String?): MonedaTipo {
            return when (value?.uppercase()) {
                "USD" -> USD
                "PEN", "SOLES" -> PEN
                else -> PEN // Por defecto PEN (soles peruanos)
            }
        }
    }

    /**
     * Obtiene el símbolo de la moneda
     */
    fun getSymbol(): String {
        return when (this) {
            PEN -> "S/"
            USD -> "$"
        }
    }
}

/**
 * Tipo de recomendación de producto
 */
enum class TipoRecomendacion {
    /** Producto similar en la misma tienda */
    ALTERNATIVA_MISMA_TIENDA,

    /** Producto similar en otra tienda */
    ALTERNATIVA_OTRA_TIENDA,

    /** Producto ecológico equivalente */
    PRODUCTO_ECO_EQUIVALENTE,

    /** Marca con mejor huella de carbono */
    MARCA_SOSTENIBLE;

    companion object {
        /**
         * Convierte un string a TipoRecomendacion
         * @param value String que representa el tipo de recomendación
         * @return TipoRecomendacion correspondiente
         */
        fun fromString(value: String?): TipoRecomendacion? {
            return when (value?.uppercase()) {
                "ALTERNATIVA_MISMA_TIENDA" -> ALTERNATIVA_MISMA_TIENDA
                "ALTERNATIVA_OTRA_TIENDA" -> ALTERNATIVA_OTRA_TIENDA
                "PRODUCTO_ECO_EQUIVALENTE" -> PRODUCTO_ECO_EQUIVALENTE
                "MARCA_SOSTENIBLE" -> MARCA_SOSTENIBLE
                else -> null
            }
        }
    }

    /**
     * Obtiene una descripción legible del tipo de recomendación
     */
    fun getDescription(): String {
        return when (this) {
            ALTERNATIVA_MISMA_TIENDA -> "Alternativa en la misma tienda"
            ALTERNATIVA_OTRA_TIENDA -> "Alternativa en otra tienda"
            PRODUCTO_ECO_EQUIVALENTE -> "Producto ecológico equivalente"
            MARCA_SOSTENIBLE -> "Marca más sostenible"
        }
    }
}

