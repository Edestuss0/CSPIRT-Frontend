package com.cpirt.app.domain.classes.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.repository.IClassRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllClassesUseCase @Inject constructor(
    private val repository: IClassRepository
) {
    suspend operator fun invoke(force: Boolean): Flow<AppResult<List<SchoolClass>>> {
        return repository.getAllClasses(force)
    }
}