package com.cpirt.app.domain.schedule.entity

import com.cpirt.app.domain.user.entity.UserPersonalInfo
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleLesson(
    val id: Int,
    val type: ScheduleLessonType,
    val baseId: Int,
    val classId: Int,
    val className: String,
    val day: ScheduleDay,
    val lessonNumber: Int,
    val subject: String,
    val teacherId: Int,
    val teacher: UserPersonalInfo?,
    val room: Int,
    val startTime: String,
    val endTime: String,
    val description: String,
)

@Serializable
enum class ScheduleLessonType {
    BASE, CURRENT, PLANNED
}

fun ScheduleLessonType.toLabel(): String = when (this) {
    ScheduleLessonType.PLANNED -> "Запланированное"
    ScheduleLessonType.BASE -> "Базовое"
    ScheduleLessonType.CURRENT -> "Текущее"
}

@Serializable
enum class ScheduleDay() {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

fun ScheduleDay.toLabel(): String = when (this) {
    ScheduleDay.MONDAY -> "Понедельник"
    ScheduleDay.TUESDAY -> "Вторник"
    ScheduleDay.WEDNESDAY -> "Среда"
    ScheduleDay.THURSDAY -> "Четверг"
    ScheduleDay.FRIDAY -> "Пятница"
    ScheduleDay.SATURDAY -> "Суббота"
    ScheduleDay.SUNDAY -> "Воскресенье"
}
