package com.cpirt.app.features.home.viewmodel

import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.events.entity.EventsView
import com.cpirt.app.domain.user.entity.UserInfo

data class HomeState(
    val isError: Boolean = false,
    val isLoading: Boolean = false,
    val schoolClassInfo: SchoolClass? = null,
    val userInfo: UserInfo? = null,
    val events: EventsView? = null,
    val classes: List<SchoolClass>? = null,
    val addEventState: AddEventState = AddEventState()
)

data class AddEventState(
    val show: Boolean = false,
    val title: String = "",
    val description: String = "",
    val startedAt: String = "",
    val ratingReward: String = "",
    val classes: List<Int> = emptyList()
)
