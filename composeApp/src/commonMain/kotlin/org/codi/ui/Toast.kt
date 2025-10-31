package org.codi.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.codi.theme.CodiThemeValues
import androidx.compose.foundation.shape.RoundedCornerShape

object ToastManager {
    data class Toast(val message: String, val durationMs: Long = 3000L)

    // observable state for the current toast
    var currentToast by mutableStateOf<Toast?>(null)
        private set

    fun show(message: String, durationMs: Long = 3000L) {
        currentToast = Toast(message, durationMs)
    }

    internal fun clear() {
        currentToast = null
    }
}

@Composable
fun ToastHost() {
    val toast = ToastManager.currentToast

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = toast != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
        ) {
            // keep a local copy so we can launch a coroutine tied to this toast
            val t = remember { toast }
            t?.let { data ->
                LaunchedEffect(data) {
                    delay(data.durationMs)
                    ToastManager.clear()
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(bottom = 28.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp)),
                    color = CodiThemeValues.colorScheme.primary,
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = data.message,
                        modifier = Modifier.padding(12.dp),
                        color = CodiThemeValues.colorScheme.onPrimary,
                        style = CodiThemeValues.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
