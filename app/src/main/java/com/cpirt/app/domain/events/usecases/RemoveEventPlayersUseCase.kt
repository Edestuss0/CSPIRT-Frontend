package com.cpirt.app.domain.events.usecases

import com.cpirt.app.domain.events.repository.IEventsRepository
import javax.inject.Inject

class RemoveEventPlayersUseCase @Inject constructor(
    private val repository: IEventsRepository
) {
    suspend operator fun invoke(id: Int, players: List<Int>) {
        when {
            id < 0 -> throw IllegalArgumentException("Укажите корректное мероприятие")
            players.isEmpty() -> throw IllegalArgumentException("Выберите участников мероприятия")
            else -> repository.removeEventPlayers(id, players)
        }
    }
}