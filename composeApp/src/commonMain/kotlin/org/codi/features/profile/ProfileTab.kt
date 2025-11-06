package org.codi.features.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.core.screen.Screen

// Screen wrapper para ProfileScreen
object ProfileScreenWrapper : Screen {
    @Composable
    override fun Content() {
        ProfileScreen()
    }
}

object ProfileTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            return remember {
                TabOptions(
                    index = 4u,
                    title = "Perfil",
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        // Envolver ProfileScreen con un Navigator para permitir navegación
        Navigator(ProfileScreenWrapper)
    }
}

