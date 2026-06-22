package com.cpirt.app.domain.user.entity

import kotlinx.serialization.Serializable

@Serializable
data class Complaint(
    val id: Int,
    val authorId: Int,
    val targetId: Int,
    val content: String,
    val createdAt: String,
    val authorName: String,
    val targetName: String
)
