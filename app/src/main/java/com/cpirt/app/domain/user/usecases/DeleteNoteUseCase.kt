package com.cpirt.app.domain.user.usecases

import com.cpirt.app.domain.user.repository.IUserRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(id: Int, userId: Int) {
        repository.deleteNote(id, userId)
    }
}