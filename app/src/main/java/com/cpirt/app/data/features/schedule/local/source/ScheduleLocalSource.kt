package com.cpirt.app.data.features.schedule.local.source

import com.cpirt.app.data.features.schedule.local.dao.ScheduleLessonDao
import com.cpirt.app.data.features.schedule.local.entity.ScheduleLessonEntity
import com.cpirt.app.domain.schedule.entity.ScheduleLesson
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ScheduleLocalSource @Inject constructor(
    private val dao: ScheduleLessonDao
) {
    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
    }

    suspend fun getByClass(id: Int): List<ScheduleLesson> {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getByClassId(id)
        return cached.map { Json.decodeFromString<ScheduleLesson>(it.json) }
    }

    suspend fun getByTeacher(id: Int): List<ScheduleLesson> {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getByTeacherId(id)
        return cached.map { Json.decodeFromString<ScheduleLesson>(it.json) }
    }

    suspend fun insert(toAdd: ScheduleLesson) {
        dao.insertSchedule(ScheduleLessonEntity(
            id = toAdd.id,
            json = Json.encodeToString(toAdd),
            classId = toAdd.classId,
            teacherId = toAdd.teacherId
        ))
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}