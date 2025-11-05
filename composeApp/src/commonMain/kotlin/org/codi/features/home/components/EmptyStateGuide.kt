package org.codi.features.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.codi.theme.CodiThemeValues
import org.codi.theme.PrimaryGreen
import org.codi.theme.SecondaryGreen

@Composable
fun EmptyStateGuide() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Ilustración - Ícono grande circular
            Surface(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                color = SecondaryGreen.copy(alpha = 0.15f)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCodeScanner,
                        contentDescription = "Escanear recibo",
                        tint = PrimaryGreen,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de bienvenida
            Text(
                text = "¡Bienvenido a Codi!",
                style = CodiThemeValues.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                ),
                color = CodiThemeValues.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            // Submensaje motivacional
            Text(
                text = "Comienza tu impacto ambiental",
                style = CodiThemeValues.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = PrimaryGreen,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Mensaje de guía
            Text(
                text = "Escanea tu primer recibo y descubre cuánto CO₂ estás ahorrando con tus compras sostenibles.",
                style = CodiThemeValues.typography.bodyLarge,
                color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Indicación visual del botón central
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = SecondaryGreen.copy(alpha = 0.1f)
                ),
                shape = CodiThemeValues.shapes.large
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ícono de cámara
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = PrimaryGreen
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Texto de instrucción
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Presiona el botón verde",
                            style = CodiThemeValues.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = CodiThemeValues.colorScheme.onBackground
                        )
                        Text(
                            text = "Toca el botón central para escanear",
                            style = CodiThemeValues.typography.bodyMedium,
                            color = CodiThemeValues.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Texto adicional de motivación
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = SecondaryGreen,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Es rápido y fácil",
                    style = CodiThemeValues.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = SecondaryGreen
                )
            }
        }
    }
}

