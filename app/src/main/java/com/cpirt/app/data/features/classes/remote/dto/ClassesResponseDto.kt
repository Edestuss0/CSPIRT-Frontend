package com.cpirt.app.data.features.classes.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponseDto(
    @SerialName("Classes")
    val schoolClasses: List<SchoolClassDto>
)
