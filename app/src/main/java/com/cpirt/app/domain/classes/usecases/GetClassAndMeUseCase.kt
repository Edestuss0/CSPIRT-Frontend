package com.cpirt.app.domain.classes.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.repository.IClassRepository
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassAndMeUseCase @Inject constructor(
    private val userRepository: IUserRepository,
    private val classRepository: IClassRepository
) {
    data class classData(val schoolClass: SchoolClass, val me: UserInfo)

    operator fun invoke(id: Int, force: Boolean): Flow<AppResult<classData>> = flow {
        emitAll(
            classRepository.getClass(id, force).combine(userRepository.getMe(force)) { classRes, userRes ->
                when {
                    classRes is AppResult.Success && userRes is AppResult.Success ->
                        AppResult.Success(classData(classRes.data, userRes.data))
                    classRes is AppResult.Error && userRes !is AppResult.Error ->
                        AppResult.Error(null, classRes.message)
                    classRes !is AppResult.Error && userRes is AppResult.Error ->
                        AppResult.Error(null, userRes.message)
                    else ->
                        AppResult.Loading
                }
            }
        )
    }
}