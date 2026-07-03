package com.cpirt.app.data.features.events.local.source

import com.cpirt.app.data.features.events.local.dao.EventsCacheDao
import com.cpirt.app.data.features.events.local.entity.EventsCacheEntity
import com.cpirt.app.domain.events.entity.Event
import kotlinx.serialization.json.Json
import javax.inject.Inject

class EventsLocalSource @Inject constructor(
    private val dao: EventsCacheDao
) {
    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
    }


    suspend fun getAll(): List<Event> {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getAll()
        return cached.map {
            Json.decodeFromString<Event>(it.json)
        }
    }

    suspend fun insert(event: Event) {
        dao.insert(EventsCacheEntity(
            id = event.id,
            json = Json.encodeToString(event),
            cachedAt = System.currentTimeMillis()
        ))
    }

    suspend fun invalidate(id: Int) {
        dao.invalidate(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}