package org.codi

import android.content.Context

object AppContextHolder {
    lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}

