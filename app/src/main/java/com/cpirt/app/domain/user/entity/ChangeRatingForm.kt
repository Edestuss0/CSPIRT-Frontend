package com.cpirt.app.domain.user.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class ChangeRatingForm(
    val reason: String,
    val targetLogin: String,
    val rating: Int
)
