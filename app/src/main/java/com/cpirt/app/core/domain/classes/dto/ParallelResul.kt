package com.cpirt.app.core.domain.classes.dto

sealed class ParallelResult {
    object Loading: ParallelResult()
    data class Success(
        val data: ParallelDto
    ): ParallelResult()
    data class Error(
        val message: String,
        val data: ParallelDto?
    ): ParallelResult()
}