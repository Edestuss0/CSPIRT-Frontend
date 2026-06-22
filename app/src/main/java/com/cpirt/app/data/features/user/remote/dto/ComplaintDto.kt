package com.cpirt.app.data.features.user.remote.dto

import com.cpirt.app.domain.user.entity.Complaint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComplaintDto(
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
) {
    fun toDomain(): Complaint {
        return Complaint(
            id = id,
            authorId = authorId,
            targetId = targetId,
            content = content,
            createdAt = createdAt,
            authorName = authorName,
            targetName = targetName,
        )
    }
}
