package org.codi.features.promos.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.data.api.models.Promocion
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

@Composable
fun PromocionItem(
    promocion: Promocion,
    puntosUsuario: Int,
    onCanjear: (String) -> Unit,
    onVerDetalle: (String) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }
    val canCanjear = puntosUsuario >= promocion.boletasRequeridas

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = 2.dp,
        onClick = { onVerDetalle(promocion.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con logo de tienda
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo de tienda (placeholder)
                    Surface(
                        color = SecondaryGreen.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = null,
                                tint = SecondaryGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Text(
                        text = promocion.tienda?.nombre ?: "Tienda",
                        style = CodiThemeValues.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                }

                // Badge de tipo de promoción
                Surface(
                    color = SecondaryGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = promocion.tipoPromocion,
                        style = CodiThemeValues.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = CodiThemeValues.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Título
            Text(
                text = promocion.titulo,
                style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = CodiThemeValues.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            promocion.descripcion?.let { desc ->
                Text(
                    text = desc,
                    style = CodiThemeValues.typography.bodyMedium,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Requisitos y botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Boletas requeridas: ${promocion.boletasRequeridas}",
                        style = CodiThemeValues.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    if (!canCanjear && promocion.disponible != false) {
                        Text(
                            text = "Te faltan ${promocion.boletasRequeridas - puntosUsuario} boletas",
                            style = CodiThemeValues.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                    // Mostrar si ya fue canjeada
                    if (promocion.disponible == false) {
                        Text(
                            text = "✓ Ya canjeaste esta promoción",
                            style = CodiThemeValues.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Button(
                    onClick = { showDialog = true },
                    enabled = canCanjear && promocion.activa && promocion.disponible != false,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = if (promocion.disponible == false) "Canjeada" else "Canjear",
                        style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }

    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Confirmar canje",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                Column {
                    Text(
                        text = "¿Deseas canjear esta promoción?",
                        style = CodiThemeValues.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = promocion.titulo,
                        style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SecondaryGreen
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCanjear(promocion.id)
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen)
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

