package org.codi.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class CodiApplication : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
    }

    override fun newImageLoader(): ImageLoader {
        // Configurar OkHttpClient con timeouts y configuración para URLs externas
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .addInterceptor { chain ->
                // Agregar headers para mejorar compatibilidad con Wikipedia y otros sitios
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Android) CodiApp/1.0")
                    .header("Accept", "image/webp,image/apng,image/*,*/*;q=0.8")
                    .build()
                chain.proceed(request)
            }
            .build()

        return ImageLoader.Builder(this)
            .okHttpClient(okHttpClient)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25) // Usar 25% de RAM disponible
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024) // 50 MB
                    .build()
            }
            .respectCacheHeaders(false) // No respetar cache headers para forzar carga
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .crossfade(true) // Animación suave al cargar
            .crossfade(300)
            .logger(DebugLogger()) // Para debugging
            .build()
    }
}

