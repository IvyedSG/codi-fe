package org.codi.features.profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codi.data.api.models.ProfileResponse
import org.codi.features.profile.ProfileViewModel
import org.codi.theme.CodiThemeValues

@Composable
fun ProfileContent(
    profileData: ProfileResponse,
    viewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val userData = profileData.data?.datosUsuario
    val stats = profileData.data?.estadisticas

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(bottom = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        UserInfoCard(
            nombre = userData?.nombre ?: "",
            apellido = userData?.apellido ?: "",
            correo = userData?.correo ?: "",
            onEditClick = { viewModel.openEditDialog() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PersonalStatsCard(
            cantidadRecibos = stats?.cantidadRecibos ?: 0,
            cantidadRecibosVerdes = stats?.cantidadRecibosVerdes ?: 0,
            co2Total = stats?.co2Total ?: 0.0,
            co2Promedio = stats?.co2Promedio ?: 0.0
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Configuración",
            style = CodiThemeValues.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = CodiThemeValues.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        SettingsSection()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE53935),
                contentColor = Color.White
            ),
            shape = CodiThemeValues.shapes.medium
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cerrar Sesión",
                style = CodiThemeValues.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = LocalContentColor.current
            )
        }
    }
}
