package org.codi.features.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.codi.features.history.components.HistoryContent
import org.codi.theme.CodiThemeValues

object HistoryTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Receipt)
            return remember {
                TabOptions(
                    index = 3u,
                    title = "Historial",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(HistoryTabScreen())
    }
}

class HistoryTabScreen : Screen {
    @Composable
    override fun Content() {
        HistoryScreen()
    }
}

@Composable
fun HistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CodiThemeValues.colorScheme.background)
    ) {
        // TopBar estándar
        org.codi.common.components.TopBar(
            title = "Historial"
        )

        // Contenido modularizado
        HistoryContent()
    }
}

