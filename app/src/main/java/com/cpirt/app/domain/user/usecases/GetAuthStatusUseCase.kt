package com.cpirt.app.domain.user.usecases

import com.cpirt.app.domain.user.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAuthStatusUseCase @Inject constructor(
    private val userRepository: IUserRepository
){
    operator fun invoke(): Flow<Boolean?> {
        return userRepository.getAuthorizedStatus()
    }
}