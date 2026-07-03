package com.cpirt.app.data.features.schedule.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.data.features.schedule.local.source.ScheduleLocalSource
import com.cpirt.app.data.features.schedule.remote.source.ScheduleRemoteSource
import com.cpirt.app.domain.schedule.entity.ScheduleLesson
import com.cpirt.app.domain.schedule.repository.IScheduleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class ScheduleRepositoryImpl @Inject constructor(
    private val remote: ScheduleRemoteSource,
    private val local: ScheduleLocalSource
) : IScheduleRepository {
    override suspend fun getScheduleByClass(id: Int, force: Boolean): Flow<AppResult<List<ScheduleLesson>>> = flow {
        val cached = local.getByClass(id)
        if (!(cached.isNullOrEmpty()) && !force) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remote.getScheduleByClassId(id)
            response.forEach { local.insert(it) }
            emit(AppResult.Success(response))
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            if (e is IOException) {
                emit(
                    AppResult.Error(
                        message = "Нет соединения с сервером",
                        data = cached
                    )
                )
                return@flow
            }

            emit(AppResult.Error(
                message = "Ошибка при попытке получения расписания",
                data = cached
            ))
        }
    }

    override suspend fun getScheduleByTeacher(id: Int, force: Boolean): Flow<AppResult<List<ScheduleLesson>>> = flow {
        val cached = local.getByTeacher(id)
        if (!(cached.isNullOrEmpty()) && !force) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remote.getScheduleByTeacherId(id)
            response.forEach { local.insert(it) }
            emit(AppResult.Success(response))
        } catch (e: Exception) {
            if (e is CancellationException) throw e

            if (e is IOException) {
                emit(
                    AppResult.Error(
                        message = "Нет соединения с сервером",
                        data = cached
                    )
                )
                return@flow
            }

            emit(AppResult.Error(
                message = "Ошибка при попытке получения расписания",
                data = cached
            ))
        }
    }

}