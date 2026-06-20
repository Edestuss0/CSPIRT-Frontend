package com.cpirt.app.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Parallel(
    @SerialName("Id")
    val id: Int,
    @SerialName("Name")
    val name: String,
    @SerialName("BestClass")
    val bestClass: SchoolClass?,
    @SerialName("Classes")
    val classes: List<SchoolClass>?,
)