package com.cpirt.app.core.domain.classes.dto

import com.cpirt.app.entities.SchoolClass

sealed class ClassResult {
    object Loading: ClassResult()
    data class Success(
        val data: SchoolClass,
    ): ClassResult()
    data class Error(
        val message: String,
        val data: SchoolClass?
    ): ClassResult()
}