package org.codi.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
    // Contenedor circular fijo; dentro colocamos la imagen con padding para preservar aspecto
    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        if (logoUrl.isNullOrBlank()) {
            // Fallback: inicial
            Text(
                text = storeName.firstOrNull()?.uppercase() ?: "T",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            // Cargamos la imagen centrada y recortada dentro del círculo para llenar el espacio
            SubcomposeAsyncImage(
                model = logoUrl,
                contentDescription = "Logo $storeName",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp), // espacio interior ligero
                contentScale = ContentScale.Crop, // llenar el círculo recortando si es necesario
            ) {
                val state = painter.state
                if (state is coil.compose.AsyncImagePainter.State.Loading || state is coil.compose.AsyncImagePainter.State.Empty) {
                    // placeholder: inicial pequeña
                    Text(
                        text = storeName.firstOrNull()?.uppercase() ?: "T",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    SubcomposeAsyncImageContent()
                }
            }
        }
    }
}
