package com.cpirt.app.core.domain.classes.repository

import com.cpirt.app.core.domain.classes.dto.ClassResult
import com.cpirt.app.core.domain.classes.dto.ClassesResult
import com.cpirt.app.core.domain.classes.dto.ParallelResult
import com.cpirt.app.core.domain.classes.dto.ParallelsResult
import com.cpirt.app.entities.SchoolClass
import kotlinx.coroutines.flow.Flow

interface IClassRepository {
    suspend fun getMyClass(force: Boolean = false): Flow<ClassResult>
    suspend fun getClass(id: Int, force: Boolean = false): Flow<ClassResult>
    suspend fun getParallels(force: Boolean = false): Flow<ParallelsResult>
    suspend fun getParallelClasses(id: Int, force: Boolean = false): Flow<ClassesResult>
    suspend fun getParallel(id: Int, force: Boolean = false): Flow<ParallelResult>
}