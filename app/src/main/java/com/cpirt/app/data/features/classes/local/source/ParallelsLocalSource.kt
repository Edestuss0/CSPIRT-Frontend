package com.cpirt.app.data.features.classes.local.source

import android.util.Log
import com.cpirt.app.data.features.classes.local.dao.ParallelsCacheDao
import com.cpirt.app.data.features.classes.local.entity.ParallelsCacheEntity
import com.cpirt.app.data.features.classes.remote.dto.ParallelDto
import com.cpirt.app.domain.classes.entity.Parallel
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ParallelsLocalSource @Inject constructor(
    private val dao: ParallelsCacheDao
) {
    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
    }

    suspend fun get(id: Int): Parallel? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.get(id)
        return cached?.let {
            runCatching { Json.decodeFromString<Parallel>(cached.json) }.getOrNull()
        }
    }

    suspend fun getAll(): List<Parallel> {
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

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun insert(form: Parallel) {
        dao.insertParallel(
            ParallelsCacheEntity(
                id = form.id,
                json = Json.encodeToString(form),
                cachedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun invalidateParallel(id: Int) {
        dao.invalidateParallel(id)
    }
}