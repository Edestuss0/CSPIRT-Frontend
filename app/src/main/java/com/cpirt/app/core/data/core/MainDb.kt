package com.cpirt.app.core.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cpirt.app.core.data.users.UserCacheDao
import com.cpirt.app.core.data.users.UserCacheEntity

@Database(
    entities = [UserCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MainDb : RoomDatabase() {
    abstract fun userCacheDao(): UserCacheDao
}