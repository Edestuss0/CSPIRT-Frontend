package com.cpirt.app.data.features.events.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompleteEventDto(
    @SerialName("ratingReward")
    val reward: Int
)
