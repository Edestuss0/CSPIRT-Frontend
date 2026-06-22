package com.cpirt.app.data.features.classes.remote.dto

import com.cpirt.app.data.features.user.remote.dto.UserPersonalInfoDto
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.user.entity.UserPersonalInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SchoolClassDto(
    @SerialName("Id")
    val id: Int,
    @SerialName("Name")
    val name: String,
    @SerialName("Grade")
    val grade: Int,
    @SerialName("Letter")
    val letter: String,
    @SerialName("FirstQuarterComplete")
    val firstQuarterComplete: Int,
    @SerialName("SecondQuarterComplete")
    val secondQuarterComplete: Int,
    @SerialName("ThirdQuarterComplete")
    val thirdQuarterComplete: Int,
    @SerialName("QuarterComplete")
    val quarterComplete: Int,
    @SerialName("TeacherLogin")
    val teacherLogin: String,
    @SerialName("Teacher")
    val teacher: UserPersonalInfoDto?,
    @SerialName("Members")
    val members: List<UserPersonalInfoDto>,
    @SerialName("UserTotalRating")
    val userTotalRating: Int,
    @SerialName("ClassTotalRating")
    val classTotalRating: Int
) {
    fun toDomain(): SchoolClass {
        return SchoolClass(
            id = id,
            name = name,
            grade = grade,
            letter = letter,
            firstQuarterComplete = firstQuarterComplete,
            secondQuarterComplete = secondQuarterComplete,
            thirdQuarterComplete = thirdQuarterComplete,
            quarterComplete = quarterComplete,
            teacherLogin = teacherLogin,
            teacher = teacher?.toDomain(),
            members = members.map { it.toDomain() },
            userTotalRating = userTotalRating,
            classTotalRating = classTotalRating
        )
    }
}