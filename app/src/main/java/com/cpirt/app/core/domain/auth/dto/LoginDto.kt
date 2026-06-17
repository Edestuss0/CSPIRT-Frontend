package com.cpirt.app.core.domain.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val login: String,
    val password: String
)