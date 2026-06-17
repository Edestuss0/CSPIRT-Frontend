package com.cpirt.app.core.domain.profile.repository

import com.cpirt.app.entities.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface IProfileRepository {
    suspend fun getMe(): UserInfo
    fun getMeData(): StateFlow<UserInfo?>
}