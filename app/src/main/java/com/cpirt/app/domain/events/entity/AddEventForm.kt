package com.cpirt.app.domain.events.entity

data class AddEventForm(
    val title: String,
    val description: String,
    val startedAt: String,
    val classes: List<Int>,
    val ratingReward: Int
)


