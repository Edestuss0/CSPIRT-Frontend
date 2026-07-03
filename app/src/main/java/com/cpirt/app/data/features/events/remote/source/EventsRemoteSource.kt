package com.cpirt.app.data.features.events.remote.source

import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.data.api.ApiClient
import com.cpirt.app.data.features.events.remote.dto.EventDto
import com.cpirt.app.data.features.events.remote.dto.toDomain
import com.cpirt.app.domain.events.entity.Event
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

class EventsRemoteSource @Inject constructor(
    private val apiClient: ApiClient
) {
    val client = apiClient.client

    suspend fun getAll(): List<Event> {
        val response = client.get("$API_URL/api/events")
        if (!(response.status.isSuccess())) {
            throw ServerException(response.status.value)
        }
        return response.body<List<EventDto>>().map { it.toDomain() }
    }
}