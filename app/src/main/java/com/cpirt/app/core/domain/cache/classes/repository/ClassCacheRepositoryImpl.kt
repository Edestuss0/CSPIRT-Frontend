package com.cpirt.app.core.domain.cache.classes.repository

import android.util.Log
import com.cpirt.app.core.data.classes.ClassCacheDao
import com.cpirt.app.core.data.classes.ClassCacheEntity
import com.cpirt.app.core.domain.cache.user.repository.UserCacheRepositoryImpl
import com.cpirt.app.entities.SchoolClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.onFailure

class ClassCacheRepositoryImpl @Inject constructor(
    private val dao: ClassCacheDao
) : IClassCacheRepository {

    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
        private val scope = CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        )
    }

    override suspend fun get(id: Int): SchoolClass? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getClass(id)
        return cached?.let {
            runCatching {
                Json.decodeFromString<SchoolClass>(cached.json)
            }.getOrNull()
        }
    }

    override suspend fun insert(classToAdd: SchoolClass) {
        dao.insertClass(
            ClassCacheEntity(
                id = classToAdd.id,
                json = Json.encodeToString(classToAdd),
                cachedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }

    override suspend fun invalidateClass(id: Int) {
        dao.invalidateClass(id)
    }

    override fun observe(id: Int): Flow<SchoolClass?> {
        scope.launch {
           dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        }
        return dao.observeClass(id).map { entity ->
            entity?.let {
                runCatching { Json.decodeFromString<SchoolClass>(entity.json) }.getOrNull()
            }
        }
    }

    override suspend fun isCacheValid(id: Int): Boolean {
        val cached = dao.getClass(id) ?: return false
        return System.currentTimeMillis() - cached.cachedAt < CACHE_TTL_MS
    }

    override suspend fun insertСlasses(classesToAdd: List<SchoolClass>) {
        val classes = classesToAdd.map {
            ClassCacheEntity(
                id = it.id,
                json = Json.encodeToString(it),
                cachedAt = System.currentTimeMillis()
            )
        }
        dao.insertClasses(classes)
    }

    override suspend fun getClasses(ids: List<Int>): List<SchoolClass> {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getClasses(ids)
        return cached.mapNotNull {
            runCatching {
                Json.decodeFromString<SchoolClass>(it.json)
            }.onFailure { exception ->
                Log.e("CACHE_SERIALIZATION", "Не удалось распарсить класс с id=${it.id}", exception)
            }.getOrNull()
        }
    }
}