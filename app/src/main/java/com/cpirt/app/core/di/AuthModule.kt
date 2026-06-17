package com.cpirt.app.core.di

import com.cpirt.app.core.domain.auth.repository.AuthorizationRepositoryImpl
import com.cpirt.app.core.domain.auth.repository.IAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthorizationRepositoryImpl
    ): IAuthRepository
}