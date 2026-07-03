package com.cpirt.app.core.di

import com.cpirt.app.data.features.schedule.repository.ScheduleRepositoryImpl
import com.cpirt.app.domain.schedule.repository.IScheduleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ScheduleModule {
    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        impl: ScheduleRepositoryImpl
    ): IScheduleRepository
}