package com.cpirt.app.data.features.events.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cpirt.app.data.features.events.local.entity.EventsCacheEntity

@Dao
interface EventsCacheDao {
    @Query("SELECT * FROM events")
    suspend fun getAll(): List<EventsCacheEntity>

    @Query("DELETE FROM events WHERE id = :id")
    suspend fun invalidate(id: Int)

    @Query("DELETE FROM events")
    suspend fun clearAll()

    @Query("DELETE FROM events WHERE cachedAt < :threshold")
    suspend fun clearExpired(threshold: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventsCacheEntity)
}