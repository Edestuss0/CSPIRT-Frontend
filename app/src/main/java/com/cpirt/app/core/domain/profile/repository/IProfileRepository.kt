package com.cpirt.app.core.domain.profile.repository

import com.cpirt.app.core.domain.profile.dto.ProfileResult
import com.cpirt.app.entities.UserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IProfileRepository {
    suspend fun getMe(force: Boolean): Flow<ProfileResult>
}