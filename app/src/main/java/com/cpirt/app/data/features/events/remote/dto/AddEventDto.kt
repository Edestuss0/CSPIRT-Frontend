package com.cpirt.app.data.features.events.remote.dto

import com.cpirt.app.domain.events.entity.AddEventForm
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddEventDto(
    @SerialName("Title")
    val title: String,
    @SerialName("Description")
    val description: String,
    @SerialName("Classes")
    val classes: List<Int>,
    @SerialName("StartedAt")
    val startedAt: String,
    @SerialName("RatingReward")
    val ratingReward: Int
)

fun AddEventForm.toDto(): AddEventDto {
    return AddEventDto(
        title = title,
        description = description,
        classes = classes,
        startedAt = startedAt,
        ratingReward = ratingReward
    )
}