package com.cpirt.app.data.features.classes.local.source

import android.util.Log
import com.cpirt.app.data.features.classes.local.dao.ClassCacheDao
import com.cpirt.app.data.features.classes.local.entity.ClassCacheEntity
import com.cpirt.app.domain.classes.entity.SchoolClass
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ClassLocalSource @Inject constructor(
    private val dao: ClassCacheDao
) {
    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
    }

    suspend fun get(id: Int): SchoolClass? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getClass(id)
        return cached?.let {
            runCatching {
                Json.decodeFromString<SchoolClass>(cached.json)
            }.getOrNull()
        }
    }

    suspend fun getAll(): List<SchoolClass> {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        val cached = dao.getAllClasses()
        return cached.mapNotNull {
            runCatching {
                Json.decodeFromString<SchoolClass>(it.json)
            }.onFailure { exception ->
                Log.e("CACHE_SERIALIZATION", "Не удалось распарсить класс с id=${it.id}", exception)
            }.getOrNull()
        }
    }

    suspend fun insert(classToAdd: SchoolClass) {
        dao.insertClass(
            ClassCacheEntity(
                id = classToAdd.id,
                json = Json.encodeToString(classToAdd),
                cachedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun invalidateClass(id: Int) {
        dao.invalidateClass(id)
    }

    suspend fun insertСlasses(classesToAdd: List<SchoolClass>) {
        val classes = classesToAdd.map {
            ClassCacheEntity(
                id = it.id,
                json = Json.encodeToString(it),
                cachedAt = System.currentTimeMillis()
            )
        }
        dao.insertClasses(classes)
    }

    suspend fun getClasses(ids: List<Int>): List<SchoolClass> {
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