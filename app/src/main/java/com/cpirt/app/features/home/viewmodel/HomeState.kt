package com.cpirt.app.features.home.viewmodel

import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.events.entity.EventsView
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.ui.components.snackbar.AppSnackbarVisuals

data class HomeState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val schoolClassInfo: SchoolClass? = null,
    val userInfo: UserInfo? = null,
    val events: EventsView? = null,
    val snackbarMessage: AppSnackbarVisuals?  = null
)
