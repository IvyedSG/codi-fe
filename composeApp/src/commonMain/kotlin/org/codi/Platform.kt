package org.codi

interface Platform {
    val name: String
}


expect fun getPlatform(): Platform
