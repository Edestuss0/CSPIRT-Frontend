package com.cpirt.app.core.domain.classes.dto

import com.cpirt.app.entities.SchoolClass
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponseDto(
    @SerialName("Classes")
    val schoolClasses: List<SchoolClass>
)
