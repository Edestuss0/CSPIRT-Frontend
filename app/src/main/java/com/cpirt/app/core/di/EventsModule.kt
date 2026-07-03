package com.cpirt.app.core.di

import com.cpirt.app.data.features.events.repository.EventsRepositoryImpl
import com.cpirt.app.domain.events.repository.IEventsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EventsModule {
    @Binds @Singleton
    abstract fun bindEventsRepository(
        impl: EventsRepositoryImpl
    ): IEventsRepository
}