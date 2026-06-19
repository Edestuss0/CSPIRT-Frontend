package com.cpirt.app.core.domain.my_class.repository

import com.cpirt.app.entities.SchoolClass

interface IMyClassRepository {
    suspend fun getMyClass(force: Boolean = false): SchoolClass
}