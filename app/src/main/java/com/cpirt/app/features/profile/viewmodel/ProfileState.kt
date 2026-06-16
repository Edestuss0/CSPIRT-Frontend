package com.cpirt.app.features.profile.viewmodel

import com.cpirt.app.entities.UserInfo
import com.cpirt.app.ui.components.AppSnackbarVisuals

data class ProfileState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = null,
    val snackbarMessage: AppSnackbarVisuals?  = null
)
