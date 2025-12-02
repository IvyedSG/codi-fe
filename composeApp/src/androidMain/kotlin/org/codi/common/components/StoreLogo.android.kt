package org.codi.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent

@Composable
actual fun StoreLogo(
    logoUrl: String?,
    storeName: String,
    modifier: Modifier,
    size: Dp
) {
    val shape = CircleShape
    // Contenedor circular con fondo blanco para mejor visibilidad de logos
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (logoUrl.isNullOrBlank()) {
            // Fallback: ícono de tienda
            Icon(
                imageVector = Icons.Default.Store,
                contentDescription = storeName,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(size * 0.5f)
            )
        } else {
            // Cargamos la imagen con manejo explícito de estados
            SubcomposeAsyncImage(
                model = logoUrl,
                contentDescription = "Logo $storeName",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp), // padding para que el logo no toque los bordes
                contentScale = ContentScale.Fit, // Mantener proporción sin recortar
            ) {
                val state = painter.state
                when (state) {
                    is coil.compose.AsyncImagePainter.State.Loading -> {
                        // Indicador de carga
                        CircularProgressIndicator(
                            modifier = Modifier.size(size * 0.4f),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    }
                    is coil.compose.AsyncImagePainter.State.Error -> {
                        // Fallback en caso de error: ícono de tienda
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = storeName,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(size * 0.5f)
                        )
                    }
                    else -> {
                        // Imagen cargada exitosamente
                        SubcomposeAsyncImageContent()
                    }
                }
            }
        }
    }
}
