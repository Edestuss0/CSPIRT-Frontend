package com.cpirt.app.domain.user.usecases

import com.cpirt.app.domain.user.entity.LoginForm
import com.cpirt.app.domain.user.repository.IUserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(form: LoginForm): String {
        return userRepository.login(form)
    }
}