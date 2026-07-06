package com.cpirt.app.domain.classes.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.repository.IClassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetClassesByIdsUseCase @Inject constructor(
    private val repository: IClassRepository
) {
    operator fun invoke(classes: List<Int>, force: Boolean): Flow<AppResult<List<SchoolClass>>> = flow {
        repository.getAllClasses(force).collect { result ->
            when {
                result is AppResult.Success -> {
                    emit(AppResult.Success(result.data.filter { classes.contains(it.id) }))
                }
                result is AppResult.Error -> {
                    emit(AppResult.Error(
                        message = result.message,
                        data = if (result.data != null) result.data.filter { classes.contains(it.id) } else null
                    ))
                }
                result is AppResult.Loading -> {
                    emit(AppResult.Loading)
                }
            }
        }
    }
}