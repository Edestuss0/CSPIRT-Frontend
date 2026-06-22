package com.cpirt.app.domain.classes.entity

import kotlinx.serialization.Serializable

@Serializable
data class Parallel(
    val id: Int,
    val name: String,
    val bestClass: SchoolClass?,
    val classes: List<Int>?,
)