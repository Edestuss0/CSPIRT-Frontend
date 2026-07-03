package com.cpirt.app.data.features.schedule.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleLessonEntity(
    @PrimaryKey
    val id: Int,
    val json: String,
    val classId: Int,
    val teacherId: Int,
    val cachedAt: Long = System.currentTimeMillis()
)
