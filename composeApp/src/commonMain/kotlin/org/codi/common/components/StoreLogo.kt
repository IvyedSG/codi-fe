// ...existing code...
package org.codi.common.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Composable multiplataforma para mostrar el logo de una tienda remoto con fallback.
 * Implementación específica para cada plataforma en `actual` files.
 */
@Composable
expect fun StoreLogo(
    logoUrl: String?,
    storeName: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp
)
// ...existing code...

