package com.cpirt.app.data.features.user.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cpirt.app.data.features.user.local.entity.UserCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCacheDao {
    @Query("SELECT * FROM user_cache WHERE userId = :id")
    fun observeUser(id: Int): Flow<UserCacheEntity?>

    @Query("SELECT * FROM user_cache WHERE userId = :id")
    suspend fun getUser(id: Int): UserCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserCacheEntity)

    @Query("DELETE FROM user_cache WHERE userId = :id")
    suspend fun invalidateUser(id: Int)

    @Query("DELETE FROM user_cache")
    suspend fun clearAll()

    @Query("DELETE FROM user_cache WHERE cachedAt < :threshold")
    suspend fun clearExpired(threshold: Long)

    @Query("SELECT * FROM user_cache WHERE isMe = 1")
    suspend fun getMe(): UserCacheEntity?
}