package com.cpirt.app.domain.user.usecases

import com.cpirt.app.domain.user.entity.AddComplaintForm
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.repository.IUserRepository
import javax.inject.Inject

class AddComplaintUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(form: AddComplaintForm, user: UserPersonalInfo?): String {
        user?.canAddComplaint(form.targetId)?.let {
            if (!it) {
                throw IllegalArgumentException("Вы не имеете права добавлять жалобы")
            }
        }

        if (form.content.isBlank()) throw IllegalArgumentException("Текст жалобы не может быть пустым")

        return userRepository.addComplaint(form)
    }
}