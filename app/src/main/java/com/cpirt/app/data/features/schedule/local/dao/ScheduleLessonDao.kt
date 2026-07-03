package com.cpirt.app.data.features.schedule.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cpirt.app.data.features.schedule.local.entity.ScheduleLessonEntity

@Dao
interface ScheduleLessonDao {
    @Query("SELECT * FROM schedule WHERE classId = :id")
    suspend fun getByClassId(id: Int): List<ScheduleLessonEntity>

    @Query("SELECT * FROM schedule WHERE teacherId = :id")
    suspend fun getByTeacherId(id: Int): List<ScheduleLessonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(form: ScheduleLessonEntity)

    @Query("DELETE FROM schedule")
    suspend fun clearAll()

    @Query("DELETE FROM class_cache WHERE cachedAt < :threshold")
    suspend fun clearExpired(threshold: Long)
}