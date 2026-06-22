package com.cpirt.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cpirt.app.data.features.classes.local.dao.ClassCacheDao
import com.cpirt.app.data.features.classes.local.entity.ClassCacheEntity
import com.cpirt.app.data.features.classes.local.dao.ParallelsCacheDao
import com.cpirt.app.data.features.classes.local.entity.ParallelsCacheEntity
import com.cpirt.app.data.features.user.local.dao.UserCacheDao
import com.cpirt.app.data.features.user.local.entity.UserCacheEntity

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