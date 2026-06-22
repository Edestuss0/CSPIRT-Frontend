package com.cpirt.app.domain.classes.entity

import com.cpirt.app.domain.user.entity.UserPersonalInfo
import kotlinx.serialization.Serializable

@Serializable
data class SchoolClass(
    val id: Int,
    val name: String,
    val grade: Int,
    val letter: String,
    val firstQuarterComplete: Int,
    val secondQuarterComplete: Int,
    val thirdQuarterComplete: Int,
    val quarterComplete: Int,
    val teacherLogin: String,
    val teacher: UserPersonalInfo?,
    val members: List<UserPersonalInfo>,
    val userTotalRating: Int,
    val classTotalRating: Int
)