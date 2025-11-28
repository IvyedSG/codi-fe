package org.codi.features.promos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import org.codi.data.storage.TokenStorage
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.theme.SecondaryGreen
import org.codi.ui.ViewModelStore

data class PromoDetailScreen(val promoId: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val promoViewModel = ViewModelStore.getPromoViewModel()
        val scope = rememberCoroutineScope()

        var promoDetail by remember { mutableStateOf<org.codi.data.api.models.Promocion?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(promoId) {
            scope.launch {
                isLoading = true
                try {
                    val userId = TokenStorage.getUserId() ?: ""
                    val result = promoViewModel.repository.getPromocionDetalle(promoId, userId)
                    result.fold(
                        onSuccess = { response ->
                            promoDetail = response.data
                            isLoading = false
                        },
                        onFailure = { error ->
                            errorMessage = error.message ?: "Error al cargar el detalle"
                            isLoading = false
                        }
                    )
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Error desconocido"
                    isLoading = false
                }
            }
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SecondaryGreen)
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "Error",
                            color = Color.Red,
                            style = CodiThemeValues.typography.bodyLarge
                        )
                        Button(
                            onClick = { navigator.pop() },
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryGreen)
                        ) {
                            Text("Volver")
                        }
                    }
                }
            }
            promoDetail != null -> {
                PromoDetailContent(
                    promo = promoDetail!!,
                    onNavigateBack = { navigator.pop() },
                    onShare = { /* Compartir promo */ },
                    onCanjear = {
                        scope.launch {
                            promoViewModel.canjearPromocion(promoId)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromoDetailContent(
    promo: org.codi.data.api.models.Promocion,
    onNavigateBack: () -> Unit,
    onShare: () -> Unit,
    onCanjear: () -> Unit
) {
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle de tu promo",
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
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onShare) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartir",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryGreen,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Barra inferior con cantidad y botón de canjear
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Selector de cantidad
                    Row(
                        modifier = Modifier
                            .weight(0.35f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(CodiThemeValues.colorScheme.background),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text(
                                text = "−",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (quantity > 1) CodiThemeValues.colorScheme.onBackground else Color.Gray
                            )
                        }

                        Text(
                            text = quantity.toString(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = CodiThemeValues.colorScheme.onBackground
                        )

                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text(
                                text = "+",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = CodiThemeValues.colorScheme.onBackground
                            )
                        }
                    }

                    // Botón de canjear
                    Button(
                        onClick = onCanjear,
                        modifier = Modifier
                            .weight(0.65f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecondaryGreen
                        ),
                        shape = RoundedCornerShape(24.dp),
                        enabled = promo.activa && promo.disponible != false
                    ) {
                        Text(
                            text = if (promo.disponible == false) "Ya Canjeada" else "Canjear Ahora",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(CodiThemeValues.colorScheme.background)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Imagen principal / Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(SecondaryGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                // Logo de la tienda
                promo.tienda?.let { tienda ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 4.dp
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = PrimaryGreen,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                        Text(
                            text = tienda.nombre,
                            style = CodiThemeValues.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = PrimaryGreen
                        )
                    }
                }
            }

            // Badge "Compra ahora" y "Más promos"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        color = if (promo.activa) SecondaryGreen.copy(alpha = 0.2f) else Color.Gray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (promo.activa) "Disponible" else "No disponible",
                            style = CodiThemeValues.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (promo.activa) PrimaryGreen else Color.Gray,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                // Badge tipo de promoción
                Surface(
                    color = SecondaryGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = promo.tipoPromocion,
                        style = CodiThemeValues.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = PrimaryGreen,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.1f)
            )

            // Información del producto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Vendido por
                promo.tienda?.let { tienda ->
                    Text(
                        text = "Vendido por ${tienda.nombre}",
                        style = CodiThemeValues.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                // Título
                Text(
                    text = promo.titulo,
                    style = CodiThemeValues.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = CodiThemeValues.colorScheme.onBackground,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descripción
                promo.descripcion?.let { desc ->
                    Text(
                        text = desc,
                        style = CodiThemeValues.typography.bodyMedium,
                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Información adicional
                promo.boletasRequeridas?.let { boletas ->
                    if (boletas > 0) {
                        DetailInfoItem(
                            label = "Boletas requeridas:",
                            value = "$boletas boletas"
                        )
                    }
                }

                promo.validezInicio?.let { inicio ->
                    DetailInfoItem(
                        label = "Vigencia de la promo:",
                        value = "Desde ${formatFecha(inicio)}"
                    )
                }

                promo.validezFin?.let { fin ->
                    DetailInfoItem(
                        label = "Válida hasta:",
                        value = formatFecha(fin)
                    )
                }

                // Estado de disponibilidad
                if (promo.disponible == false) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = PrimaryGreen.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "✓",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryGreen
                            )
                            Column {
                                Text(
                                    text = "Ya canjeaste esta promoción",
                                    style = CodiThemeValues.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = PrimaryGreen
                                )
                                promo.fechaUso?.let { fecha ->
                                    Text(
                                        text = "Fecha de canje: ${formatFecha(fecha)}",
                                        style = CodiThemeValues.typography.bodySmall,
                                        color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun DetailInfoItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = CodiThemeValues.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = CodiThemeValues.colorScheme.onBackground
        )
        Text(
            text = value,
            style = CodiThemeValues.typography.bodyMedium,
            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

private fun formatFecha(isoString: String): String {
    return try {
        // Formato simple para mostrar la fecha
        val parts = isoString.split("T")[0].split("-")
        if (parts.size == 3) {
            "${parts[2]}/${parts[1]}/${parts[0]}"
        } else {
            isoString
        }
    } catch (e: Exception) {
        isoString
    }
}

