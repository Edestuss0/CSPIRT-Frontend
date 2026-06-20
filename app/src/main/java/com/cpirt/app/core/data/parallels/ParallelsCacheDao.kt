package com.cpirt.app.core.data.parallels

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ParallelsCacheDao {
    @Query("SELECT * FROM parallels_cache")
    suspend fun getAll(): List<ParallelsCacheEntity>

    @Query("SELECT * FROM parallels_cache WHERE id = :id")
    suspend fun get(id: Int): ParallelsCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParallel(form: ParallelsCacheEntity)

    @Query("DELETE FROM parallels_cache WHERE id = :id")
    suspend fun invalidateParallel(id: Int)

    @Query("DELETE FROM parallels_cache")
    suspend fun clearAll()

    @Query("DELETE FROM parallels_cache WHERE cachedAt < :threshold")
    suspend fun clearExpired(threshold: Long)
}