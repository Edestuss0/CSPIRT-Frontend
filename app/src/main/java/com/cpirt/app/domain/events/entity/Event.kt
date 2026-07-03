package com.cpirt.app.domain.events.entity

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Int,
    val title: String,
    val status: EventStatus,
    val ratingReward: Int,
    val description: String,
    val startedAt: String,
    val players: List<Int>,
    val classes: List<Int>
)

enum class EventStatus {
    ACTIVE, COMPLETED, SCHEDULED, UNKNOWN
}

fun EventStatus.toLabel(): String {
    return when (this) {
        EventStatus.ACTIVE -> "Активно"
        EventStatus.COMPLETED -> "Завершено"
        EventStatus.UNKNOWN -> "Неизвестно"
        EventStatus.SCHEDULED -> "Запланировано"
    }
}