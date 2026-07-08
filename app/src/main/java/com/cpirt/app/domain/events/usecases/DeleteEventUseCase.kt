package com.cpirt.app.domain.events.usecases

import com.cpirt.app.domain.events.repository.IEventsRepository
import javax.inject.Inject

class DeleteEventUseCase @Inject constructor(
    private val repository: IEventsRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.deleteEvent(id)
    }
}