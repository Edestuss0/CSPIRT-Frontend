package com.cpirt.app.ui.components

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals

enum class SnackbarMessageType {
    SUCCESS,
    ERROR,
    INFO
}

data class AppSnackbarVisuals(
    override val message: String,
    val type: SnackbarMessageType = SnackbarMessageType.INFO,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
) : SnackbarVisuals