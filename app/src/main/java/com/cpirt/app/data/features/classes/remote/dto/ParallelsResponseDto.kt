package com.cpirt.app.data.features.classes.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParallelsResponseDto(
    @SerialName("ParallelClasses")
    val parallels: List<ParallelDto>
)
