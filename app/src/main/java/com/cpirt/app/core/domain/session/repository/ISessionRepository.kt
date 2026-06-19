package com.cpirt.app.core.domain.session.repository

import kotlinx.coroutines.flow.Flow

interface ISessionRepository {
    fun getAuthorizedStatus(): Flow<Boolean?>
    suspend fun authorize(id: Int)
    suspend fun logout()
    suspend fun getId(): Flow<Int?>
}