package com.cpirt.app.data.features.events.remote.dto

import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.entity.EventStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @SerialName("ID")
    val id: Int,
    @SerialName("Title")
    val title: String,
    @SerialName("Status")
    val status: String,
    @SerialName("RatingReward")
    val ratingReward: Int,
    @SerialName("Description")
    val description: String,
    @SerialName("CreatedAt")
    val createdAt: String,
    @SerialName("StartedAt")
    val startedAt: String,
    @SerialName("Players")
    val players: List<Int>,
    @SerialName("Classes")
    val classes: List<Int>
)

fun EventDto.toDomain(): Event {
    val finalStatus = when (this.status) {
        "active" -> EventStatus.ACTIVE
        "completed" -> EventStatus.COMPLETED
        "scheduled" -> EventStatus.SCHEDULED
        else -> EventStatus.UNKNOWN
    }
    return Event(
        id = id,
        title = title,
        status = finalStatus,
        ratingReward = ratingReward,
        description = description,
        startedAt = startedAt,
        players = players,
        classes = classes
    )
}