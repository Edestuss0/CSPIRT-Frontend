package com.cpirt.app.domain.events.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.events.entity.AddEventForm
import com.cpirt.app.domain.events.entity.Event
import kotlinx.coroutines.flow.Flow

interface IEventsRepository {
    suspend fun getEvents(force: Boolean = false): Flow<AppResult<List<Event>>>
    suspend fun addEvent(form: AddEventForm)
    suspend fun addEventPlayers(id: Int, players: List<Int>)
    suspend fun removeEventPlayers(id: Int, players: List<Int>)
    suspend fun deleteEvent(id: Int)
    suspend fun completeEvent(event: Event)
}