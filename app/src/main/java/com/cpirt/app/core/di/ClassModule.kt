package com.cpirt.app.core.di

import com.cpirt.app.domain.classes.repository.IClassRepository
import com.cpirt.app.data.features.classes.repository.ClassRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassModule {
    @Binds
    @Singleton
    abstract fun bindClassRepository(
        impl: ClassRepositoryImpl
    ): IClassRepository
}