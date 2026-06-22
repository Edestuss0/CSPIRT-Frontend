package com.cpirt.app.core.entity

sealed class AppResult<out T> {
    object Loading : AppResult<Nothing>()

    data class Success<out T>(val data: T) : AppResult<T>()

    data class Error<out T>(
        val data: T? = null,
        val message: String
    ) : AppResult<T>()
}