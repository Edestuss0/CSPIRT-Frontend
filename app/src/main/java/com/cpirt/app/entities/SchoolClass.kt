package com.cpirt.app.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SchoolClass(
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
    val teacher: UserPersonalInfo?,
    @SerialName("Members")
    val members: List<UserPersonalInfo>,
    @SerialName("UserTotalRating")
    val userTotalRating: Int,
    @SerialName("ClassTotalRating")
    val classTotalRating: Int
)