package com.cpirt.app.core.domain.user.dto

import com.cpirt.app.entities.UserInfo

sealed class UserResult {
    object Loading: UserResult()
    data class Success(
        val data: UserInfo,
    ): UserResult()
    data class Error(
        val message: String,
        val oldData: UserInfo?
    ): UserResult()
}