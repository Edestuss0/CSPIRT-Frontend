package com.cpirt.app.ui.components.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.cpirt.app.ui.theme.*
@Composable
fun AppSnackbarHost(
    host: SnackbarHostState, ) {
    SnackbarHost(hostState = host) { data ->
        val appVisuals = data.visuals as? AppSnackbarVisuals

        val bgColor = when (appVisuals?.type) {
            SnackbarMessageType.INFO -> DarkInfo
            SnackbarMessageType.ERROR -> DarkError
            SnackbarMessageType.SUCCESS -> DarkSuccess
            else -> DarkInfo
        }

        Snackbar(
            snackbarData = data,
            containerColor = bgColor,
            contentColor = Color.White,
            actionColor = Color.White.copy(alpha = 0.85f),
            shape = MaterialTheme.shapes.medium
        )
    }
}
