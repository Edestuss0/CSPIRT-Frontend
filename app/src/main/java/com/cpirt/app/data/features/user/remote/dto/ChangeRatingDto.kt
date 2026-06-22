package com.cpirt.app.data.features.user.remote.dto

import com.cpirt.app.domain.user.entity.ChangeRatingForm
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

fun ChangeRatingForm.toDto(): ChangeRatingDto {
    return ChangeRatingDto(
        reason = reason,
        targetLogin = targetLogin,
        rating = rating
    )
}