package com.cpirt.app.domain.events.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
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