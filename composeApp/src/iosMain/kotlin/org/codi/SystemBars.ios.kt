package org.codi

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
actual fun SetSystemBarsColor(
    statusBarColor: Color,
    navigationBarColor: Color,
    isDarkIcons: Boolean
) {
    // iOS maneja esto automáticamente a través del Info.plist
}

