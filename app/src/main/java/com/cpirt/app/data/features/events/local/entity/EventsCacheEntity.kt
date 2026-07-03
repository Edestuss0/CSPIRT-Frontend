package com.cpirt.app.data.features.events.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("events")
data class EventsCacheEntity(
    @PrimaryKey
    val id: Int,
    val json: String,
    val cachedAt: Long = System.currentTimeMillis()
)
