package com.cpirt.app.domain.schedule.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.schedule.entity.ScheduleLesson
import com.cpirt.app.domain.schedule.repository.IScheduleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScheduleByTeacherUseCase @Inject constructor(
    private val repository: IScheduleRepository
) {
    suspend operator fun invoke(id: Int, force: Boolean): Flow<AppResult<List<ScheduleLesson>>> {
        return repository.getScheduleByTeacher(id, force)
    }
}