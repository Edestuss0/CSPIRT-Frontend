package com.cpirt.app.domain.user.entity

import com.cpirt.app.domain.events.entity.Event
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    User, Admin, Helper, Owner, Public
}

fun UserRole.toDisplayName(): String {
    return when (this) {
        UserRole.User -> "Ученик"
        UserRole.Admin -> "Учитель"
        UserRole.Helper -> "Староста"
        UserRole.Owner -> "Руководство"
        UserRole.Public -> "Публичный"
    }
}

@Serializable
data class UserPersonalInfo(
    val id: Int,
    val name: String,
    val lastName: String,
    val avatar: String?,
    val login: String,
    val rating: Int,
    val role: UserRole,
    val className: String,
    val classId: Int
) {
    val fullname: String get() = "$lastName $name"
    fun canChangeRating(targetLogin: String): Boolean = (role == UserRole.Admin || role == UserRole.Owner) && targetLogin != login
    fun canManageNotes(): Boolean = (role == UserRole.Admin || role == UserRole.Owner || role == UserRole.Helper)
    fun canAddComplaint(targetId: Int): Boolean = targetId != id
    fun canManageComplaints(): Boolean = role == UserRole.Admin || role == UserRole.Owner
}

@Serializable
data class UserInfo(
    val user: UserPersonalInfo,
    val notes: List<Note>,
    val complaints: List<Complaint>,
    val events: List<Event>,
    val classTeacher: UserPersonalInfo?
)