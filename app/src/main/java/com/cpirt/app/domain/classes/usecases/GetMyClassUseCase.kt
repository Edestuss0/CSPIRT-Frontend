package com.cpirt.app.domain.classes.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.repository.IClassRepository
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMyClassUseCase @Inject constructor(
    private val userRepository: IUserRepository,
    private val classRepository: IClassRepository
) {
    data class MyClassData(val schoolClass: SchoolClass, val me: UserInfo)

    operator fun invoke(force: Boolean): Flow<AppResult<MyClassData>> = flow {
        val id = userRepository.getMe(force).filterIsInstance<AppResult.Success<UserInfo>>().firstOrNull()?.data?.user?.classId
        if (id == null) {
            emit(AppResult.Error(null, "Класс не найден"))
            return@flow
        }
        emitAll(
            classRepository.getClass(id, force).combine(userRepository.getMe(force)) { classRes, userRes ->
                when {
                    classRes is AppResult.Success && userRes is AppResult.Success ->
                        AppResult.Success(MyClassData(classRes.data, userRes.data))
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