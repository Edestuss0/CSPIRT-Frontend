package com.cpirt.app.features.classes.viewmodel

import com.cpirt.app.domain.classes.entity.SchoolClass

data class ClassesState(
    val isLoading: Boolean = false,
    val classesData: List<SchoolClass>? = null
)

sealed class ClassesUIEventState {
    data class ShowSuccess(val message: String): ClassesUIEventState()
    data class ShowError(val message: String): ClassesUIEventState()
}