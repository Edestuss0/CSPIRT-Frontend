package com.cpirt.app.features.event.viewmodel

import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.user.entity.UserPersonalInfo

data class EventState(
    val isLoading: Boolean = false,
    val eventClasses: List<SchoolClass>? = null,
    val event: Event? = null,
    val eventPlayersState: EventPlayersState = EventPlayersState(),
    val profileInfo: UserPersonalInfo? = null
)

data class EventPlayersState(
    val toAdd: List<Int> = emptyList(),
    val toRemove: List<Int> = emptyList(),
    val current: List<Int> = emptyList()
)
