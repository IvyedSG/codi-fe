package org.codi

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
expect fun SetSystemBarsColor(
    statusBarColor: Color = Color.White,
    navigationBarColor: Color = Color.White,
    isDarkIcons: Boolean = true
)
