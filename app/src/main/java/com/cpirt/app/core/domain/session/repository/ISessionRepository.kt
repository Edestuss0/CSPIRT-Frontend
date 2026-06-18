package com.cpirt.app.core.domain.session.repository

import kotlinx.coroutines.flow.Flow

interface ISessionRepository {
    fun getAuthorizedStatus(): Flow<Boolean?>
    suspend fun authorize()
    suspend fun logout()
}