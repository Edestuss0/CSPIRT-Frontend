package com.cpirt.app.domain.events.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.repository.IEventsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: IEventsRepository
) {
    suspend operator fun invoke(force: Boolean): Flow<AppResult<List<Event>>> {
        return repository.getEvents(force)
    }
}