package com.cpirt.app.core.di

import com.cpirt.app.core.domain.profile.repository.IProfileRepository
import com.cpirt.app.core.domain.profile.repository.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {
    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        impl: ProfileRepositoryImpl
    ): IProfileRepository
}