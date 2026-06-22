package com.cpirt.app.domain.user.usecases

import com.cpirt.app.domain.user.entity.ChangeRatingForm
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.repository.IUserRepository
import javax.inject.Inject

class ChangeRatingUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(form: ChangeRatingForm, user: UserPersonalInfo?): String {
        user?.canChangeRating(form.targetLogin)?.let {
            if (!it) {
                throw IllegalArgumentException("Вы не имеете права менять рейтинг")
            }
        }
        if (form.rating < -5000 || form.rating > 5000) throw IllegalArgumentException("Рейтинг должен быть в допустимом диапазоне")
        return userRepository.changeUserRating(form)
    }
}