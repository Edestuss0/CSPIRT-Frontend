package com.cpirt.app.data.features.events.remote.source

import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.data.api.ApiClient
import com.cpirt.app.data.features.events.remote.dto.AddEventPlayersDto
import com.cpirt.app.data.features.events.remote.dto.CompleteEventDto
import com.cpirt.app.data.features.events.remote.dto.EventDto
import com.cpirt.app.data.features.events.remote.dto.toDomain
import com.cpirt.app.data.features.events.remote.dto.toDto
import com.cpirt.app.domain.events.entity.AddEventForm
import com.cpirt.app.domain.events.entity.Event
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    suspend fun addEvent(form: AddEventForm) {
        val response = client.patch("$API_URL/api/event/add") {
            contentType(ContentType.Application.Json)
            setBody(form.toDto())
        }
        if (!(response.status.isSuccess())) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun addEventPlayers(id: Int, players: List<Int>) {
        val response = client.patch("$API_URL/api/event/$id/players/add") {
            contentType(ContentType.Application.Json)
            setBody(AddEventPlayersDto(players))
        }
        if (!(response.status.isSuccess())) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun removeEventPlayers(id: Int, players: List<Int>) {
        val response = client.delete("$API_URL/api/event/$id/players/delete") {
            contentType(ContentType.Application.Json)
            setBody(AddEventPlayersDto(players))
        }
        if (!(response.status.isSuccess())) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun completeEvent(event: Event) {
        val response = client.patch("$API_URL/api/event/${event.id}/complete") {
            contentType(ContentType.Application.Json)
            setBody(CompleteEventDto(event.ratingReward))
        }
        if (!(response.status.isSuccess())) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun deleteEvent(id: Int) {
        val response = client.delete("$API_URL/api/event/delete/$id")
        if (!(response.status.isSuccess())) {
            throw ServerException(response.status.value)
        }
    }
}