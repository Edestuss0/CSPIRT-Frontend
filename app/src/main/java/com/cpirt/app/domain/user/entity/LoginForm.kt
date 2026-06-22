package com.cpirt.app.domain.user.entity

import kotlinx.serialization.Serializable

@Serializable
data class LoginForm(
    val login: String,
    val password: String
)