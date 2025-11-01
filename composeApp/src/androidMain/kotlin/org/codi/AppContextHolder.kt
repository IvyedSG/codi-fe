package org.codi

import android.annotation.SuppressLint
import android.content.Context

/**
 * Holder para mantener el contexto de la aplicación Android
 * Usado para acceder al contexto desde código compartido
 *
 * NOTA: Usamos Application Context, no Activity Context,
 * por lo que NO hay riesgo de memory leak
 */
@SuppressLint("StaticFieldLeak")
object AppContextHolder {
    private var context: Context? = null

    fun setContext(appContext: Context) {
        context = appContext.applicationContext
    }

    fun getContext(): Context {
        return context ?: throw IllegalStateException("El contexto de la aplicación no ha sido inicializado")
    }
}

