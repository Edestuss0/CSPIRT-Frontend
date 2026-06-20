package com.cpirt.app.core.domain.cache.user.repository

import com.cpirt.app.core.data.users.UserCacheDao
import com.cpirt.app.core.data.users.UserCacheEntity
import com.cpirt.app.entities.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserCacheRepositoryImpl @Inject constructor(
    private val dao: UserCacheDao
) : IUserCacheRepository {
    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
        private val scope = CoroutineScope(
            SupervisorJob() + Dispatchers.IO
        )
    }

    override suspend fun isCacheValid(id: Int): Boolean {
        val cached = dao.getUser(id) ?: return false
        return System.currentTimeMillis() - cached.cachedAt < CACHE_TTL_MS
    }

    override suspend fun get(id: Int): UserInfo? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        return dao.getUser(id)?.let { entity ->
            runCatching { Json.decodeFromString<UserInfo>(entity.json) }.getOrNull()
        }
    }

    override fun observe(id: Int): Flow<UserInfo?> {
        scope.launch {
            dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        }
        return dao.observeUser(id).map { entity ->
            entity?.let {
                runCatching { Json.decodeFromString<UserInfo>(entity.json) }.getOrNull()
            }
        }
    }

    override suspend fun insert(user: UserInfo, isMe: Boolean) {
        dao.insertUser(
            UserCacheEntity(
                userId = user.user.id,
                json = Json.encodeToString(user),
                cachedAt = System.currentTimeMillis(),
                isMe = isMe
            )
        )
    }

    override suspend fun invalidateUser(id: Int) {
        dao.invalidateUser(id)
    }

    override suspend fun clearAll() {
        dao.clearAll()
    }

    override suspend fun getMe(): UserInfo? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        return dao.getMe()?.let { entity ->
            runCatching { Json.decodeFromString<UserInfo>(entity.json) }.getOrNull()
        }
    }

}