package com.cpirt.app.domain.events.usecases

import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.events.repository.IEventsRepository
import javax.inject.Inject

class CompleteEventUseCase @Inject constructor(
    private val repository: IEventsRepository
) {
    suspend operator fun invoke(event: Event) {
        repository.completeEvent(event)
    }
}