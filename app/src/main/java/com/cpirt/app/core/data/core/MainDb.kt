package com.cpirt.app.core.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cpirt.app.core.data.classes.ClassCacheDao
import com.cpirt.app.core.data.classes.ClassCacheEntity
import com.cpirt.app.core.data.parallels.ParallelsCacheDao
import com.cpirt.app.core.data.parallels.ParallelsCacheEntity
import com.cpirt.app.core.data.users.UserCacheDao
import com.cpirt.app.core.data.users.UserCacheEntity

@Database(
    entities = [UserCacheEntity::class, ClassCacheEntity::class, ParallelsCacheEntity::class],
    version = 3,
    exportSchema = false
)
abstract class MainDb : RoomDatabase() {
    abstract fun userCacheDao(): UserCacheDao
    abstract fun classCacheDao(): ClassCacheDao
    abstract fun parallelsCacheDao(): ParallelsCacheDao
}