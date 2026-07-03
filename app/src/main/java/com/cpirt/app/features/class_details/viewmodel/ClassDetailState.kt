package com.cpirt.app.features.class_details.viewmodel

import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.schedule.entity.ScheduleView

data class ClassDetailState(
    val isLoading: Boolean = false,
    val classData: SchoolClass? = null,
    val scheduleData: ScheduleView? = null,
)
