package com.cpirt.app.features.user.viewmodel

import com.cpirt.app.entities.UserInfo
import com.cpirt.app.ui.components.AppSnackbarVisuals

data class UserState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = null,
    val profileInfo: UserInfo? = null,
    val snackbarMessage: AppSnackbarVisuals? = null,
    val changeRatingState: ChangeRatingState = ChangeRatingState(),
    val addNoteState: AddNoteState = AddNoteState(),
    val addComplaintState: AddComplaintState = AddComplaintState()
)

data class ChangeRatingState(
    val show: Boolean = false,
    val reason: String = "",
    val rating: String = ""
)

data class AddNoteState(
    val show: Boolean = false,
    val content: String = ""
)

data class AddComplaintState(
    val show: Boolean = false,
    val content: String = ""
)