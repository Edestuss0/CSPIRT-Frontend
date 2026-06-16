package com.cpirt.app.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cpirt.app.ui.theme.*
@Composable
fun AppSnackbarHost(
    host: SnackbarHostState, ) {
    SnackbarHost(hostState = host) { data ->
        val appVisuals = data.visuals as? AppSnackbarVisuals

        val bgColor = when (appVisuals?.type) {
            SnackbarMessageType.INFO -> InfoBlue
            SnackbarMessageType.ERROR -> ErrorRed
            SnackbarMessageType.SUCCESS -> SuccessGreen
            else -> InfoBlue
        }

        Snackbar(
            snackbarData = data,
            containerColor = bgColor,
            contentColor = Color.White,
            actionColor = Color.White.copy(alpha = 0.85f),
            shape = RoundedCornerShape(16.dp)
        )
    }
}