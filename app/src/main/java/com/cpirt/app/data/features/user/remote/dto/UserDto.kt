package com.cpirt.app.data.features.user.remote.dto

import com.cpirt.app.data.features.events.remote.dto.EventDto
import com.cpirt.app.data.features.events.remote.dto.toDomain
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
    val notes: List<NoteDto>? = emptyList(),
    @SerialName("Complaints")
    val complaints: List<ComplaintDto>? = emptyList(),
    @SerialName("Events")
    val events: List<EventDto>? = emptyList(),
    @SerialName("ClassTeacher")
    val classTeacher: UserPersonalInfoDto? = null
) {
    fun toDomain(): UserInfo {
        return UserInfo(
            user = user.toDomain(),
            notes = notes.orEmpty().map { it.toDomain() },
            complaints = complaints.orEmpty().map { it.toDomain() },
            events = events.orEmpty().map { it.toDomain() },
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
    val avatar: UserAvatarInfoDto? = null,
    @SerialName("FullName")
    val fullName: List<FullNameSchemaDto>? = null,
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
            avatar = avatar?.takeIf { it.valid }?.string,
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
