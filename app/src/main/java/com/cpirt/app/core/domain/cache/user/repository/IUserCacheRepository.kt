package com.cpirt.app.core.domain.cache.user.repository

import com.cpirt.app.entities.UserInfo
import kotlinx.coroutines.flow.Flow

interface IUserCacheRepository {
    suspend fun isCacheValid(id: Int): Boolean
    suspend fun get(id: Int): UserInfo?
    fun observe(id: Int): Flow<UserInfo?>
    suspend fun invalidateUser(id: Int)
    suspend fun insert(user: UserInfo, isMe: Boolean = false)
    suspend fun clearAll()
    suspend fun getMe(): UserInfo?
}