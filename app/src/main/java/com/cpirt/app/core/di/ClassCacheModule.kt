package com.cpirt.app.core.di

import com.cpirt.app.core.domain.cache.classes.repository.ClassCacheRepositoryImpl
import com.cpirt.app.core.domain.cache.classes.repository.IClassCacheRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassCacheModule {
    @Binds @Singleton
    abstract fun bindsClassCacheRepository(
        impl: ClassCacheRepositoryImpl
    ): IClassCacheRepository
}