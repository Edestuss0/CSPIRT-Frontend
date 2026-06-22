package com.cpirt.app.features.my_class.viewmodel

import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.ui.components.AppSnackbarVisuals

data class MyClassState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val schoolClassInfo: SchoolClass? = null,
    val userInfo: UserInfo? = null,
    val snackbarMessage: AppSnackbarVisuals?  = null
)
