package com.cpirt.app.domain.classes.usecases

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.entity.Parallel
import com.cpirt.app.domain.classes.repository.IClassRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParallelsUseCase @Inject constructor(
    private val classRepository: IClassRepository
) {
    suspend operator fun invoke(force: Boolean): Flow<AppResult<List<Parallel>>> {
        return classRepository.getParallels(force)
    }
}