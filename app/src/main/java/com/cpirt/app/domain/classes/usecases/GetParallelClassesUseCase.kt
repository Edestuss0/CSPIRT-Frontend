package com.cpirt.app.domain.classes.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.repository.IClassRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParallelClassesUseCase @Inject constructor(
    private val classRepository: IClassRepository
) {
    suspend operator fun invoke(id: Int, force: Boolean): Flow<AppResult<List<SchoolClass>>> {
        return classRepository.getParallelClasses(id, force)
    }
}