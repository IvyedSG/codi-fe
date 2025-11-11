package org.codi.features.promos.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import org.codi.data.api.ApiClient
import org.codi.data.api.models.Promocion
import org.codi.data.storage.TokenStorage
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

@Composable
fun PromoDetailDialog(
    promocionId: String,
    onDismiss: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var promocion by remember { mutableStateOf<Promocion?>(null) }
    val scope = rememberCoroutineScope()

    // Cargar detalle al abrir el diálogo
    LaunchedEffect(promocionId) {
        scope.launch {
            try {
                val userId = TokenStorage.getUserId()
                if (userId.isNullOrBlank()) {
                    error = "Usuario no autenticado"
                    isLoading = false
                    return@launch
                }

                val response = ApiClient.router.getPromocionDetalle(promocionId, userId)
                if (response.success && response.data != null) {
                    promocion = response.data
                } else {
                    error = response.error ?: "No se pudo cargar el detalle"
                }
            } catch (e: Exception) {
                error = e.message ?: "Error al cargar el detalle"
            } finally {
                isLoading = false
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detalle de Promoción",
                        style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = CodiThemeValues.colorScheme.onBackground
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = CodiThemeValues.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Contenido
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = SecondaryGreen)
                        }
                    }
                    error != null -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = error ?: "Error desconocido",
                                style = CodiThemeValues.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    promocion != null -> {
                        PromoDetailContent(promocion = promocion!!)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón cerrar
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryGreen,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cerrar", style = CodiThemeValues.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
private fun PromoDetailContent(promocion: Promocion) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Título
        Text(
            text = promocion.titulo,
            style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )

        // Tienda
        promocion.tienda?.let { tienda ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Store,
                    contentDescription = null,
                    tint = SecondaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = tienda.nombre,
                    style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = CodiThemeValues.colorScheme.onBackground
                )
            }
        }

        HorizontalDivider()

        // Descripción
        promocion.descripcion?.let { desc ->
            DetailItem(
                icon = Icons.Default.Description,
                label = "Descripción",
                value = desc
            )
        }

        // Tipo de promoción
        DetailItem(
            icon = Icons.Default.LocalOffer,
            label = "Tipo",
            value = promocion.tipoPromocion
        )


        // Fecha de uso
        promocion.fechaUso?.let { fecha ->
            DetailItem(
                icon = Icons.Default.CalendarToday,
                label = "Canjeado el",
                value = formatearFechaCompleta(fecha)
            )
        }

        // Vigencia
        if (promocion.validezInicio != null && promocion.validezFin != null) {
            DetailItem(
                icon = Icons.Default.DateRange,
                label = "Vigencia",
                value = "${formatearFechaCompleta(promocion.validezInicio)} - ${formatearFechaCompleta(promocion.validezFin)}"
            )
        }
    }
}

@Composable
private fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f),
            )
            Text(
                text = label,
                style = CodiThemeValues.typography.labelMedium,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
        Text(
            text = value,
            style = CodiThemeValues.typography.bodyMedium,
            color = CodiThemeValues.colorScheme.onBackground
        )
    }
}

private fun formatearFechaCompleta(fechaISO: String): String {
    return try {
        val parts = fechaISO.split("T")[0].split("-")
        if (parts.size == 3) {
            val day = parts[2]
            val month = obtenerNombreMesCompleto(parts[1].toIntOrNull() ?: 1)
            val year = parts[0]
            "$day de $month de $year"
        } else {
            fechaISO.split("T").firstOrNull() ?: "Fecha no disponible"
        }
    } catch (_: Exception) {
        "Fecha no disponible"
    }
}

private fun obtenerNombreMesCompleto(mes: Int): String {
    return when (mes) {
        1 -> "Enero"
        2 -> "Febrero"
        3 -> "Marzo"
        4 -> "Abril"
        5 -> "Mayo"
        6 -> "Junio"
        7 -> "Julio"
        8 -> "Agosto"
        9 -> "Septiembre"
        10 -> "Octubre"
        11 -> "Noviembre"
        12 -> "Diciembre"
        else -> "Desconocido"
    }
}

