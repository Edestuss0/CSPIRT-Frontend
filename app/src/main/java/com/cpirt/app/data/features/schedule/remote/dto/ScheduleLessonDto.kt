package com.cpirt.app.data.features.schedule.remote.dto

import com.cpirt.app.data.features.user.remote.dto.UserPersonalInfoDto
import com.cpirt.app.domain.schedule.entity.ScheduleDay
import com.cpirt.app.domain.schedule.entity.ScheduleLesson
import com.cpirt.app.domain.schedule.entity.ScheduleLessonType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleLessonDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("Type")
    val type: String? = null,
    @SerialName("BaseScheduleID")
    val baseScheduleId: Int? = null,
    @SerialName("ClassID")
    val classId: Int,
    @SerialName("Class")
    val className: String? = null,
    @SerialName("DayOfWeek")
    val dayOfWeek: String,
    @SerialName("LessonNumber")
    val lessonNumber: Int,
    @SerialName("WeekType")
    val weekType: String = "all",
    @SerialName("Subject")
    val subject: String,
    @SerialName("TeacherID")
    val teacherId: Int,
    @SerialName("Teacher")
    val teacher: UserPersonalInfoDto? = null,
    @SerialName("Room")
    val room: Int,
    @SerialName("StartTime")
    val startTime: String,
    @SerialName("EndTime")
    val endTime: String,
    @SerialName("Description")
    val description: String = "",
    @SerialName("CreatedAt")
    val createdAt: String? = null
)

fun ScheduleLessonDto.toDomain(): ScheduleLesson {
    val domainType = this.type?.let { typeStr ->
        runCatching { ScheduleLessonType.valueOf(typeStr.uppercase()) }.getOrElse { ScheduleLessonType.BASE }
    } ?: ScheduleLessonType.BASE

    val domainDay = runCatching {
        ScheduleDay.valueOf(this.dayOfWeek.uppercase())
    }.getOrElse {
        ScheduleDay.MONDAY
    }

    return ScheduleLesson(
        id = this.id,
        type = domainType,
        baseId = this.baseScheduleId ?: 0,
        classId = this.classId,
        className = this.className ?: "",
        day = domainDay,
        lessonNumber = this.lessonNumber,
        subject = this.subject,
        teacherId = this.teacherId,
        teacher = this.teacher?.toDomain(),
        room = this.room,
        startTime = this.startTime,
        endTime = this.endTime,
        description = this.description
    )
}

