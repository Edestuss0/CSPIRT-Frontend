package com.cpirt.app.data.features.schedule.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponseDto(
    @SerialName("Schedules")
    val schedules: List<ScheduleLessonDto>,
    @SerialName("Base")
    val base: List<ScheduleLessonDto>,
    @SerialName("Planned")
    val planned: List<ScheduleLessonDto>,
    @SerialName("Current")
    val current: List<ScheduleLessonDto>
)
