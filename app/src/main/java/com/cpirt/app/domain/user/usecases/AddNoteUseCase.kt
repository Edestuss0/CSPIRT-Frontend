package com.cpirt.app.domain.user.usecases

import com.cpirt.app.domain.user.entity.AddNoteForm
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.repository.IUserRepository
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(form: AddNoteForm, user: UserPersonalInfo?): String {
        user?.canManageNotes()?.let {
            if (!it) {
                throw IllegalArgumentException("Вы не имеете права управлять заметками")
            }
        }

        if (form.content.isBlank()) throw IllegalArgumentException("Текст заметки не может быть пустым")

        return userRepository.addNote(form)
    }
}