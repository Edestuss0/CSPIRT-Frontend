package com.cpirt.app.domain.classes.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.classes.entity.Parallel
import com.cpirt.app.domain.classes.entity.SchoolClass
import kotlinx.coroutines.flow.Flow

interface IClassRepository {
    suspend fun getClass(id: Int, force: Boolean = false): Flow<AppResult<SchoolClass>>
    suspend fun getParallels(force: Boolean = false): Flow<AppResult<List<Parallel>>>
    suspend fun getParallelClasses(id: Int, force: Boolean = false): Flow<AppResult<List<SchoolClass>>>
}