package com.cpirt.app.core.di

import com.cpirt.app.core.domain.cache.user.repository.IUserCacheRepository
import com.cpirt.app.core.domain.cache.user.repository.UserCacheRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserCacheModule {
    @Binds @Singleton
    abstract fun bindUserCacheRepository(
        impl: UserCacheRepositoryImpl
    ): IUserCacheRepository
}