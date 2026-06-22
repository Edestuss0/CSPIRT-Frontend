package com.cpirt.app.domain.user.usecases

import com.cpirt.app.domain.user.repository.IUserRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke() {
        return userRepository.logout()
    }
}