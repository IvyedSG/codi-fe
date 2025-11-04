package org.codi.features.promos

import androidx.compose.ui.graphics.vector.ImageVector

// Modelos de UI para componentes estáticos (no serializables)

data class PromoCard(
    val title: String,
    val description: String,
    val icon: ImageVector
)

data class GreenReceiptPromo(
    val recibosRequired: Int,
    val title: String,
    val description: String,
    val icon: ImageVector
)

