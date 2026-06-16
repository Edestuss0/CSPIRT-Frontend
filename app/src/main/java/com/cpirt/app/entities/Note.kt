package com.cpirt.app.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @SerialName("ID")
    val id: Int,
    @SerialName("AuthorID")
    val authorId: Int,
    @SerialName("TargetID")
    val targetId: Int,
    @SerialName("Content")
    val content: String,
    @SerialName("CreatedAt")
    val createdAt: String,
    @SerialName("AuthorName")
    val authorName: String,
    @SerialName("TargetName")
    val targetName: String
)
