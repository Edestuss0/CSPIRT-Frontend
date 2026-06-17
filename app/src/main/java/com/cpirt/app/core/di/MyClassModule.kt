package com.cpirt.app.core.di

import com.cpirt.app.core.domain.my_class.repository.IMyClassRepository
import com.cpirt.app.core.domain.my_class.repository.MyClassRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MyClassModule {
    @Binds
    @Singleton
    abstract fun bindMyClassRepository(
        impl: MyClassRepositoryImpl
    ): IMyClassRepository
}