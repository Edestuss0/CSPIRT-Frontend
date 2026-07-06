package com.cpirt.app.widgets.core

import com.cpirt.app.domain.classes.usecases.GetMyClassUseCase
import com.cpirt.app.domain.schedule.usecases.GetScheduleByClassUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getMyClassUseCase(): GetMyClassUseCase
    fun getSchedule(): GetScheduleByClassUseCase
}