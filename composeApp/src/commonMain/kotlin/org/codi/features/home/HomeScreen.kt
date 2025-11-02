package org.codi.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.codi.features.home.components.HomeContent
import org.codi.theme.CodiThemeValues

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        // TopBar fijo - no hace scroll
        org.codi.common.components.TopBar(
            title = "Bienvenido a Codi"
        )

        // Contenido modularizado
        HomeContent()
    }
}
