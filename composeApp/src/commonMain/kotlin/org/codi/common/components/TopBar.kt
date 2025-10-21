package org.codi.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen

@Composable
fun TopBar(
    title: String,
    showGreenReceipts: Boolean = false,
    greenReceiptsCount: Int = 0,
    showFilter: Boolean = false,
    onFilterClick: (() -> Unit)? = null
) {
    Surface(
        color = CodiThemeValues.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Título
            Text(
                text = title,
                style = CodiThemeValues.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            // Acciones a la derecha (recibos verdes y filtro)
            if (showGreenReceipts || showFilter) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge de recibos verdes
                    if (showGreenReceipts) {
                        Surface(
                            color = PrimaryGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Eco,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "$greenReceiptsCount recibos",
                                    style = CodiThemeValues.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                    color = PrimaryGreen
                                )
                            }
                        }
                    }

                    // Botón de filtro
                    if (showFilter && onFilterClick != null) {
                        IconButton(
                            onClick = onFilterClick,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Filtrar",
                                tint = CodiThemeValues.colorScheme.onBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

