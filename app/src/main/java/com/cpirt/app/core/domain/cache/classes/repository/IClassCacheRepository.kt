package com.cpirt.app.core.domain.cache.classes.repository

import com.cpirt.app.entities.SchoolClass
import com.cpirt.app.entities.UserInfo
import kotlinx.coroutines.flow.Flow

interface IClassCacheRepository {
    suspend fun isCacheValid(id: Int): Boolean
    suspend fun get(id: Int): SchoolClass?
    fun observe(id: Int): Flow<SchoolClass?>
    suspend fun invalidateClass(id: Int)
    suspend fun insert(classToAdd: SchoolClass)
    suspend fun insertСlasses(classesToAdd: List<SchoolClass>)
    suspend fun clearAll()
    suspend fun getClasses(ids: List<Int>): List<SchoolClass>
}