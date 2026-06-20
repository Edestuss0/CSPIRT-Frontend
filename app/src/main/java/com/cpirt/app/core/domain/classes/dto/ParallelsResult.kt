package com.cpirt.app.core.domain.classes.dto

import com.cpirt.app.entities.Parallel

sealed class ParallelsResult {
    object Loading: ParallelsResult()
    data class Success(
        val data: List<Parallel>
    ): ParallelsResult()
    data class Error(
        val message: String,
        val data: List<Parallel>?
    ): ParallelsResult()
}