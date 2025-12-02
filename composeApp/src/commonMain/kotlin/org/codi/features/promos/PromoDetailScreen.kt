package org.codi.features.promos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.codi.data.api.models.Promocion
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

/**
 * Vista de detalle de promoción en pantalla completa como Dialog
 */
@Composable
fun PromoDetailFullScreen(
    promocion: Promocion,
    onDismiss: () -> Unit,
    viewModel: PromoViewModel? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        PromoDetailContent(
            promocion = promocion,
            onNavigateBack = onDismiss,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PromoDetailContent(
    promocion: Promocion,
    onNavigateBack: () -> Unit,
    viewModel: PromoViewModel? = null
) {
    val scrollState = rememberScrollState()
    var showCanjearDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    var isProcessing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Observar cambios en el estado del ViewModel para mostrar errores o éxito
    LaunchedEffect(viewModel?.state?.error) {
        viewModel?.state?.error?.let { errorMsg ->
            snackbarHostState.showSnackbar(
                message = errorMsg,
                duration = SnackbarDuration.Long
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle de promoción",
                        style = CodiThemeValues.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = CodiThemeValues.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CodiThemeValues.colorScheme.background,
                    titleContentColor = CodiThemeValues.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            // Barra inferior con botón de acción
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        if (viewModel != null && !isProcessing) {
                            showCanjearDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecondaryGreen
                    ),
                    shape = RoundedCornerShape(28.dp),
                    enabled = promocion.activa && promocion.disponible != false && viewModel != null && !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (promocion.disponible == false) "Ya Canjeada" else "Usar Promoción",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            ) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (data.visuals.message.contains("éxito", ignoreCase = true)) {
                        Color(0xFF4CAF50)
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodiThemeValues.colorScheme.background)
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            // Imagen/Banner principal
            PromoImageBanner(promocion = promocion)

            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tienda y etiqueta
                PromoHeader(promocion = promocion)

                // Título de la promoción
                Text(
                    text = promocion.titulo,
                    style = CodiThemeValues.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = CodiThemeValues.colorScheme.onBackground
                )

                // Descripción
                promocion.descripcion?.let { desc ->
                    Text(
                        text = desc,
                        style = CodiThemeValues.typography.bodyLarge,
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.1f)
                )

                // Información de vigencia
                PromoVigencia(promocion = promocion)

                // Boletas requeridas
                if (promocion.boletasRequeridas > 0) {
                    PromoRequirement(promocion = promocion)
                }

                // Fecha de uso (si ya fue canjeada)
                promocion.fechaUso?.let { fecha ->
                    PromoUsageDate(fecha = fecha)
                }

                // Estado
                PromoStatus(promocion = promocion)
            }
        }
    }

    // Diálogo de confirmación para canjear
    if (showCanjearDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isProcessing) {
                    showCanjearDialog = false
                }
            },
            title = {
                Text(
                    text = "Confirmar canje",
                    style = CodiThemeValues.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            },
            text = {
                Column {
                    Text(
                        text = "¿Deseas canjear esta promoción?",
                        style = CodiThemeValues.typography.bodyMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = promocion.titulo,
                        style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = SecondaryGreen
                    )
                    if (promocion.boletasRequeridas > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Se descontarán ${promocion.boletasRequeridas} punto(s) verde(s)",
                            style = CodiThemeValues.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isProcessing = true
                            showCanjearDialog = false

                            try {
                                viewModel?.canjearPromocion(promocion.id)

                                // Esperar un momento para que se procese
                                delay(500)

                                // Verificar si hay error en el estado
                                if (viewModel?.state?.error == null) {
                                    snackbarHostState.showSnackbar(
                                        message = "¡Promoción canjeada con éxito!",
                                        duration = SnackbarDuration.Short
                                    )
                                    delay(1500)
                                    onNavigateBack()
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar(
                                    message = "Error al canjear: ${e.message ?: "Error desconocido"}",
                                    duration = SnackbarDuration.Long
                                )
                            } finally {
                                isProcessing = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen),
                    enabled = !isProcessing
                ) {
                    if (isProcessing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Confirmar", color = Color.White)
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCanjearDialog = false },
                    enabled = !isProcessing
                ) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }
}

@Composable
private fun PromoImageBanner(promocion: Promocion) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(
                SecondaryGreen.copy(alpha = 0.1f)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Logo de la tienda en el centro
        promocion.tienda?.let { tienda ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Logo de tienda desde URL
                org.codi.common.components.StoreLogo(
                    logoUrl = tienda.urlLogo,
                    storeName = tienda.nombre,
                    modifier = Modifier.size(100.dp),
                    size = 100.dp
                )

                Text(
                    text = tienda.nombre,
                    style = CodiThemeValues.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
            }
        } ?: run {
            // Si no hay tienda, mostrar un ícono genérico
            Icon(
                imageVector = Icons.Default.LocalOffer,
                contentDescription = null,
                tint = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.size(80.dp)
            )
        }
    }
}

@Composable
private fun PromoHeader(promocion: Promocion) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Badge de tipo de promoción
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 2.dp
        ) {
            Text(
                text = promocion.tipoPromocion,
                style = CodiThemeValues.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Badge de "Canjeada" si aplica
        if (promocion.disponible == false) {
            Surface(
                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Canjeada",
                        style = CodiThemeValues.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
    }
}

@Composable
private fun PromoVigencia(promocion: Promocion) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = SecondaryGreen,
                modifier = Modifier.size(24.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Vigencia de la promoción",
                    style = CodiThemeValues.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.Black
                )
                if (promocion.validezInicio != null && promocion.validezFin != null) {
                    Text(
                        text = "Del ${formatearFecha(promocion.validezInicio)} al ${formatearFecha(promocion.validezFin)}",
                        style = CodiThemeValues.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                } else {
                    Text(
                        text = "Vigencia no especificada",
                        style = CodiThemeValues.typography.bodyMedium,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

@Composable
private fun PromoRequirement(promocion: Promocion) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Receipt,
                contentDescription = null,
                tint = SecondaryGreen,
                modifier = Modifier.size(24.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Boletas requeridas",
                    style = CodiThemeValues.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
                Text(
                    text = "${promocion.boletasRequeridas} boleta${if (promocion.boletasRequeridas > 1) "s" else ""}",
                    style = CodiThemeValues.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun PromoUsageDate(fecha: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(24.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Fecha de canje",
                    style = CodiThemeValues.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = CodiThemeValues.colorScheme.onBackground
                )
                Text(
                    text = formatearFechaCompleta(fecha),
                    style = CodiThemeValues.typography.bodyMedium,
                    color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun PromoStatus(promocion: Promocion) {
    val (statusText, statusColor, statusIcon) = when {
        promocion.disponible == false -> Triple(
            "Esta promoción ya ha sido canjeada",
            Color(0xFF4CAF50),
            Icons.Default.CheckCircle
        )
        !promocion.activa -> Triple(
            "Esta promoción ya no está activa",
            Color(0xFFF44336),
            Icons.Default.Cancel
        )
        else -> Triple(
            "Promoción disponible para canjear",
            SecondaryGreen,
            Icons.Default.CheckCircle
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = statusColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = statusIcon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = statusText,
                style = CodiThemeValues.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )
        }
    }
}

// Funciones de utilidad para formatear fechas
private fun formatearFecha(fechaISO: String): String {
    return try {
        val parts = fechaISO.split("T")[0].split("-")
        if (parts.size == 3) {
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } else {
            fechaISO.split("T").firstOrNull() ?: "Fecha no disponible"
        }
    } catch (_: Exception) {
        "Fecha no disponible"
    }
}

private fun formatearFechaCompleta(fechaISO: String): String {
    return try {
        val parts = fechaISO.split("T")[0].split("-")
        if (parts.size == 3) {
            val day = parts[2]
            val month = obtenerNombreMes(parts[1].toIntOrNull() ?: 1)
            val year = parts[0]
            "$day de $month de $year"
        } else {
            fechaISO.split("T").firstOrNull() ?: "Fecha no disponible"
        }
    } catch (_: Exception) {
        "Fecha no disponible"
    }
}

private fun obtenerNombreMes(mes: Int): String {
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

