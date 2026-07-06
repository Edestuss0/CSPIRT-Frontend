package com.cpirt.app.data.features.classes.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cpirt.app.data.features.classes.local.entity.ClassCacheEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassCacheDao {

    @Query("SELECT * FROM class_cache WHERE id = :id")
    suspend fun getClass(id: Int): ClassCacheEntity?

    @Query("SELECT * FROM class_cache WHERE id IN (:ids)")
    suspend fun getClasses(ids: List<Int>): List<ClassCacheEntity>

    @Query("SELECT * FROM class_cache")
    suspend fun getAllClasses(): List<ClassCacheEntity>

    @Query("SELECT * FROM class_cache WHERE id = :id")
    fun observeClass(id: Int): Flow<ClassCacheEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(form: ClassCacheEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(form: List<ClassCacheEntity>)

    @Query("DELETE FROM class_cache WHERE id = :id")
    suspend fun invalidateClass(id: Int)

    @Query("DELETE FROM class_cache")
    suspend fun clearAll()

    @Query("DELETE FROM class_cache WHERE cachedAt < :threshold")
    suspend fun clearExpired(threshold: Long)
}