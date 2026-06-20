package com.cpirt.app.features.parallels.viewmodel

import com.cpirt.app.entities.Parallel
import com.cpirt.app.ui.components.AppSnackbarVisuals

data class ParallelsState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val snackbarMessage: AppSnackbarVisuals? = null,
    val parallels: List<Parallel>? = null
)
