package com.cpirt.app.core.data.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "class_cache")
data class ClassCacheEntity(
    @PrimaryKey
    val id: Int,
    val json: String,
    val cachedAt: Long = System.currentTimeMillis()
)
