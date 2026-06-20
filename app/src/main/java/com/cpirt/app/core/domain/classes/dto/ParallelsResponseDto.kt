package com.cpirt.app.core.domain.classes.dto

import com.cpirt.app.entities.Parallel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParallelsResponseDto(
    @SerialName("ParallelClasses")
    val parallels: List<ParallelDto>
)
