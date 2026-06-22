package com.cpirt.app.domain.user.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AddNoteForm(
    val authorId: Int,
    val targetId: Int,
    val content: String,
    val createdAt: String,
    val authorName: String,
    val targetName: String
)
