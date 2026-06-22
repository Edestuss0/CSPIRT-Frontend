package com.cpirt.app.data.features.user.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_cache")
data class UserCacheEntity(
    @PrimaryKey
    val userId: Int,
    val json: String,
    val cachedAt: Long = System.currentTimeMillis(),
    val isMe: Boolean
)