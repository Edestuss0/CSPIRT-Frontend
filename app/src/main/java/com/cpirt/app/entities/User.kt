package com.cpirt.app.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FullNameSchema(
    @SerialName("Name")
    val name: String,
    @SerialName("LastName")
    val lastName: String
)

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
    @SerialName("Id")
    val id: Int,
    @SerialName("Name")
    val name: String,
    @SerialName("LastName")
    val lastName: String,
    @SerialName("Avatar")
    val avatar: UserAvatarInfo,
    @SerialName("FullName")
    val fullName: List<FullNameSchema>?,
    @SerialName("Login")
    val login: String,
    @SerialName("Rating")
    val rating: Int,
    @SerialName("Role")
    val role: UserRole,
    @SerialName("Class")
    val className: String,
    @SerialName("ClassID")
    val classId: Int
)

@Serializable
data class UserInfo(
    @SerialName("User")
    val user: UserPersonalInfo,
    @SerialName("Notes")
    val notes: List<Note>,
    @SerialName("Complaints")
    val complaints: List<Complaint>,
    @SerialName("Events")
    val events: List<Event>,
    @SerialName("ClassTeacher")
    val classTeacher: UserPersonalInfo?
)

@Serializable
data class UserAvatarInfo(
    @SerialName("String")
    val string: String,
    @SerialName("Valid")
    val valid: Boolean
)