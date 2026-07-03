package com.cpirt.app.domain.schedule.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.schedule.entity.ScheduleLesson
import kotlinx.coroutines.flow.Flow

interface IScheduleRepository {
    suspend fun getScheduleByClass(id: Int, force: Boolean): Flow<AppResult<List<ScheduleLesson>>>
    suspend fun getScheduleByTeacher(id: Int, force: Boolean): Flow<AppResult<List<ScheduleLesson>>>
}