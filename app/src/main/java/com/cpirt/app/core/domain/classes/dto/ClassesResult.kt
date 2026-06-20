package com.cpirt.app.core.domain.classes.dto

import com.cpirt.app.entities.SchoolClass

sealed class ClassesResult {
    object Loading: ClassesResult()
    data class Success(
        val data: List<SchoolClass>,
    ): ClassesResult()
    data class Error(
        val message: String,
        val data: List<SchoolClass>?
    ): ClassesResult()
}