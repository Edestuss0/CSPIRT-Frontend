package com.cpirt.app.core.domain.user.repository

import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getAuthorizedStatus(): Flow<Boolean?>
    suspend fun authorize()
    suspend fun logout()
}