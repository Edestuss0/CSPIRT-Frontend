package com.cpirt.app.core.domain.classes.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParallelDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("Name")
    val name: String,
    @SerialName("BestClassId")
    val bestClassId: Int,
    @SerialName("ClassesIds")
    val classes: List<Int>?
)