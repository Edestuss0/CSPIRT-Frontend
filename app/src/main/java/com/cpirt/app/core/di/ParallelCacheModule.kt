package com.cpirt.app.core.di

import com.cpirt.app.core.domain.cache.parallels.repository.IParallelsCacheRepository
import com.cpirt.app.core.domain.cache.parallels.repository.ParallelsCacheCacheRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ParallelCacheModule {
    @Binds @Singleton
    abstract fun bindParallelsCacheRepository(
        impl: ParallelsCacheCacheRepositoryImpl
    ): IParallelsCacheRepository
}