package com.cpirt.app.domain.user.entity

data class AddComplaintForm(
    val authorId: Int,
    val targetId: Int,
    val content: String,
    val createdAt: String,
    val authorName: String,
    val targetName: String
)
