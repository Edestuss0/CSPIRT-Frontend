package com.cpirt.app.data.features.events.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.data.features.events.local.source.EventsLocalSource
import com.cpirt.app.data.features.events.remote.source.EventsRemoteSource
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.repository.IEventsRepository
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventsRepositoryImpl @Inject constructor(
    private val remote: EventsRemoteSource,
    private val local: EventsLocalSource
) : IEventsRepository {
    override suspend fun getEvents(force: Boolean): Flow<AppResult<List<Event>>> = flow {
        val cached = local.getAll()
        if (!(cached.isNullOrEmpty()) && !force) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remote.getAll()
            response.forEach { local.insert(it) }
            emit(AppResult.Success(response))
        } catch (e: Exception) {
            when {
                e is CancellationException -> {
                    throw e
                }
                e is IOException -> {
                    emit(AppResult.Error(
                        message = "Нет соединения с интернетом",
                        data = cached
                    ))
                }
                else -> {
                    emit(AppResult.Error(
                        message = "Ошибка при попытке получения мероприятий",
                        data = cached
                    ))
                }
            }
        }
    }

}