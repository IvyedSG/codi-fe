package org.codi.features.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.codi.features.home.HomeTab
import org.codi.features.promos.PromosTab
import org.codi.features.upload.UploadTab
import org.codi.features.history.HistoryTab
import org.codi.features.profile.ProfileTab
import org.codi.theme.CodiThemeValues
import org.codi.theme.SecondaryGreen

object HomeTabNavigator : Screen {
    @Composable
    override fun Content() {
        TabNavigator(HomeTab) {
            Scaffold(
                containerColor = CodiThemeValues.colorScheme.background,
                contentWindowInsets = WindowInsets(0, 0, 0, 0), // Sin insets adicionales, lo manejamos manualmente
                bottomBar = {
                    NavigationBar(
                        containerColor = Color.White,
                        contentColor = CodiThemeValues.colorScheme.primary,
                        tonalElevation = 0.dp // Sin elevación para que se vea plano
                    ) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(PromosTab)
                        TabNavigationItem(UploadTab)
                        TabNavigationItem(HistoryTab)
                        TabNavigationItem(ProfileTab)
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top)) // Solo padding superior
                ) {
                    CurrentTab()
                }
            }
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            icon = {
                tab.options.icon?.let { painter ->
                    Icon(
                        painter = painter,
                        contentDescription = tab.options.title
                    )
                }
            },
            label = {
                Text(
                    text = tab.options.title,
                    style = CodiThemeValues.typography.labelSmall
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = CodiThemeValues.colorScheme.primary,
                selectedTextColor = CodiThemeValues.colorScheme.primary,
                unselectedIconColor = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f),
                unselectedTextColor = CodiThemeValues.colorScheme.onSurface.copy(alpha = 0.6f),
                indicatorColor = SecondaryGreen.copy(alpha = 0.3f)
            )
        )
    }
}
