package com.cpirt.app.core.domain.cache.parallels.repository

import android.util.Log
import com.cpirt.app.core.data.parallels.ParallelsCacheDao
import com.cpirt.app.core.data.parallels.ParallelsCacheEntity
import com.cpirt.app.entities.Parallel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ParallelsCacheCacheRepositoryImpl @Inject constructor(
    private val dao: ParallelsCacheDao
) : IParallelsCacheRepository {

    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
        private val scope = CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        )
    }

    override suspend fun get(id: Int): Parallel? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.get(id)
        return cached?.let {
            runCatching { Json.decodeFromString<Parallel>(cached.json) }.getOrNull()
        }
    }

    override suspend fun getAll(): List<Parallel> {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getAll()
        return cached.mapNotNull {
            runCatching {
                Json.decodeFromString<Parallel>(it.json)
            }.onFailure { exception ->
                Log.e("CACHE_SERIALIZATION", "Не удалось распарсить параллель с id=${it.id}", exception)
            }.getOrNull()
        }
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }

    override suspend fun insert(form: Parallel) {
        dao.insertParallel(
            ParallelsCacheEntity(
                id = form.id,
                json = Json.encodeToString(form),
                cachedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun invalidateParallel(id: Int) {
        dao.invalidateParallel(id)
    }
}