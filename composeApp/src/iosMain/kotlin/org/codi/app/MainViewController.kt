package org.codi.app

import androidx.compose.ui.window.ComposeUIViewController
import org.codi.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
