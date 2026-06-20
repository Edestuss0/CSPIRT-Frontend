package com.cpirt.app.core.data.parallels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parallels_cache")
data class ParallelsCacheEntity(
    @PrimaryKey
    val id: Int,
    val json: String,
    val cachedAt: Long = System.currentTimeMillis()
)
