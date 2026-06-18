package com.cpirt.app.core.domain.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeRatingDto(
    @SerialName("reason")
    val reason: String,
    @SerialName("target_login")
    val targetLogin: String,
    @SerialName("rating")
    val rating: Int
)
