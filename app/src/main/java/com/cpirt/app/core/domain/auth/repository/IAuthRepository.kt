package com.cpirt.app.core.domain.auth.repository

import com.cpirt.app.core.domain.auth.dto.LoginDto

interface IAuthRepository {
    suspend fun login(form: LoginDto): String
    suspend fun logout()
}