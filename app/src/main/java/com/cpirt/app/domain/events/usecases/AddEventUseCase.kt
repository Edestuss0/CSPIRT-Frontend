package com.cpirt.app.domain.events.usecases

import com.cpirt.app.domain.events.entity.AddEventForm
import com.cpirt.app.domain.events.repository.IEventsRepository
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    private val repository: IEventsRepository
) {
    suspend operator fun invoke(form: AddEventForm) {
        when {
            form.classes.isEmpty() -> throw IllegalArgumentException("Выберите классы, учавствующие в мероприятии")
            form.title.isBlank() -> throw IllegalArgumentException("Введите название мероприятия")
            form.description.length < 10 -> throw IllegalArgumentException("Описание мероприятия должно быть длиннее 10 символов")
            form.startedAt.isBlank() -> throw IllegalArgumentException("Выберите дату начала мероприятия")
            form.ratingReward <= 0 -> throw IllegalArgumentException("Введите корректную награду за участие в мероприятии")
            else -> repository.addEvent(form)
        }
    }
}