package com.cpirt.app.core.domain.cache.parallels.repository

import com.cpirt.app.entities.Parallel

interface IParallelsCacheRepository {
    suspend fun get(id: Int): Parallel?
    suspend fun getAll(): List<Parallel>
    suspend fun invalidateParallel(id: Int)
    suspend fun clearAll()
    suspend fun insert(form: Parallel)
}