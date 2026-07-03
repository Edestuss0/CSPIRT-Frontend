package com.cpirt.app.features.user.viewmodel

import com.cpirt.app.domain.user.entity.UserInfo

data class UserState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val userInfo: UserInfo? = null,
    val profileInfo: UserInfo? = null,
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

sealed class UserUIEventState {
    data class ShowSuccess(val message: String): UserUIEventState()
    data class ShowError(val message: String): UserUIEventState()
}