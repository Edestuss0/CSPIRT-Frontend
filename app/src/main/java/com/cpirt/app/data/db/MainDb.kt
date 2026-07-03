package com.cpirt.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cpirt.app.data.features.classes.local.dao.ClassCacheDao
import com.cpirt.app.data.features.classes.local.entity.ClassCacheEntity
import com.cpirt.app.data.features.classes.local.dao.ParallelsCacheDao
import com.cpirt.app.data.features.classes.local.entity.ParallelsCacheEntity
import com.cpirt.app.data.features.events.local.dao.EventsCacheDao
import com.cpirt.app.data.features.events.local.entity.EventsCacheEntity
import com.cpirt.app.data.features.schedule.local.dao.ScheduleLessonDao
import com.cpirt.app.data.features.schedule.local.entity.ScheduleLessonEntity
import com.cpirt.app.data.features.user.local.dao.UserCacheDao
import com.cpirt.app.data.features.user.local.entity.UserCacheEntity

@Database(
    entities = [EventsCacheEntity::class, UserCacheEntity::class, ClassCacheEntity::class, ParallelsCacheEntity::class, ScheduleLessonEntity::class],
    version = 6,
    exportSchema = false
)
abstract class MainDb : RoomDatabase() {
    abstract fun userCacheDao(): UserCacheDao
    abstract fun classCacheDao(): ClassCacheDao
    abstract fun parallelsCacheDao(): ParallelsCacheDao
    abstract fun scheduleCacheDao(): ScheduleLessonDao
    abstract fun eventsCacheDao(): EventsCacheDao
}