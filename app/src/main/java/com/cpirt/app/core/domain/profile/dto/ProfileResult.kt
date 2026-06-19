package com.cpirt.app.core.domain.profile.dto

import com.cpirt.app.entities.UserInfo

sealed class ProfileResult {
    object Loading: ProfileResult()
    data class Success(
        val data: UserInfo,
        val fromCache: Boolean
    ): ProfileResult()
    data class Error(
        val message: String,
        val oldData: UserInfo?
    ): ProfileResult()
}