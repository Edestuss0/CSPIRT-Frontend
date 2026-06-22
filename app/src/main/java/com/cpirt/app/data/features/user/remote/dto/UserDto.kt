package com.cpirt.app.data.features.user.remote.dto

import com.cpirt.app.domain.events.entity.Event
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import com.cpirt.app.domain.user.entity.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDto(
    @SerialName("User")
    val user: UserPersonalInfoDto,
    @SerialName("Notes")
    val notes: List<NoteDto>,
    @SerialName("Complaints")
    val complaints: List<ComplaintDto>,
    @SerialName("Events")
    val events: List<Event>,
    @SerialName("ClassTeacher")
    val classTeacher: UserPersonalInfoDto?
) {
    fun toDomain(): UserInfo {
        return UserInfo(
            user = user.toDomain(),
            notes = notes.map { it.toDomain() },
            complaints = complaints.map { it.toDomain() },
            events = events,
            classTeacher = classTeacher?.toDomain()
        )
    }
}

@Serializable
data class UserPersonalInfoDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("Name")
    val name: String,
    @SerialName("LastName")
    val lastName: String,
    @SerialName("Avatar")
    val avatar: UserAvatarInfoDto,
    @SerialName("FullName")
    val fullName: List<FullNameSchemaDto>?,
    @SerialName("Login")
    val login: String,
    @SerialName("Rating")
    val rating: Int,
    @SerialName("Role")
    val role: UserRoleDto,
    @SerialName("Class")
    val className: String,
    @SerialName("ClassID")
    val classId: Int
) {
    fun toDomain(): UserPersonalInfo {
        return UserPersonalInfo(
            id = id,
            name = name,
            lastName = lastName,
            avatar = if (avatar.valid) avatar.string else null,
            login = login,
            rating = rating,
            role = role.toDomain(),
            className = className,
            classId = classId
        )
    }
}

@Serializable
data class FullNameSchemaDto(
    @SerialName("Name")
    val name: String,
    @SerialName("LastName")
    val lastName: String
)

@Serializable
data class UserAvatarInfoDto(
    @SerialName("String")
    val string: String,
    @SerialName("Valid")
    val valid: Boolean
)

@Serializable
enum class UserRoleDto {
    User, Admin, Helper, Owner, Public
}

fun UserRoleDto.toDomain(): UserRole {
    return when {
        this == UserRoleDto.User -> UserRole.User
        this == UserRoleDto.Helper -> UserRole.Helper
        this == UserRoleDto.Admin -> UserRole.Admin
        this == UserRoleDto.Owner -> UserRole.Owner
        this == UserRoleDto.Public -> UserRole.Public
        else -> UserRole.User
    }
}