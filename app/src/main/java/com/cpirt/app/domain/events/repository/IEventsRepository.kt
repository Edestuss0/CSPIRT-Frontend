package com.cpirt.app.domain.events.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.events.entity.Event
import kotlinx.coroutines.flow.Flow

interface IEventsRepository {
    suspend fun getEvents(force: Boolean = false): Flow<AppResult<List<Event>>>
}