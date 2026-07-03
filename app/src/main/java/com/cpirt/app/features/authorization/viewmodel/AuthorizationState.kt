package com.cpirt.app.features.authorization.viewmodel

import com.cpirt.app.ui.components.snackbar.AppSnackbarVisuals

data class AuthorizationState(
    val loginInput: String = "",
    val passwordInput: String = "",
    val isError: Boolean = false,
    val snackbarMessage: AppSnackbarVisuals? = null,
    val isLoading: Boolean = false
)


