package com.cpirt.app.domain.user.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: IUserRepository
) {
    suspend operator fun invoke(id: Int, force: Boolean): Flow<AppResult<UserInfo>> {
        return userRepository.getUserById(id, force)
    }
}