package com.cpirt.app.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
    val login: String,
    val password: String
)