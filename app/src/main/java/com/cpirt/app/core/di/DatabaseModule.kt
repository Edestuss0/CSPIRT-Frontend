package com.cpirt.app.core.di

import android.content.Context
import androidx.room.Room
import com.cpirt.app.data.features.classes.local.dao.ClassCacheDao
import com.cpirt.app.data.db.MainDb
import com.cpirt.app.data.features.classes.local.dao.ParallelsCacheDao
import com.cpirt.app.data.features.user.local.dao.UserCacheDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDb(
        @ApplicationContext context: Context
    ): MainDb = Room.databaseBuilder(
        context.applicationContext,
        MainDb::class.java,
        "main.db"
    ).fallbackToDestructiveMigration().build()

    @Provides @Singleton
    fun provideUserCacheDao(
        db: MainDb
    ): UserCacheDao = db.userCacheDao()

    @Provides @Singleton
    fun provideClassCacheDao(
        db: MainDb
    ): ClassCacheDao = db.classCacheDao()

    @Provides @Singleton
    fun provideParallelsCacheDao(
        db: MainDb
    ): ParallelsCacheDao = db.parallelsCacheDao()
}