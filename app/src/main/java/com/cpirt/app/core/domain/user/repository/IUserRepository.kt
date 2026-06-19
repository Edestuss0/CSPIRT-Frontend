package com.cpirt.app.core.domain.user.repository

import com.cpirt.app.core.domain.user.dto.AddComplaintDto
import com.cpirt.app.core.domain.user.dto.AddNoteDto
import com.cpirt.app.core.domain.user.dto.ChangeRatingDto
import com.cpirt.app.entities.UserInfo

interface IUserRepository {
    suspend fun getUserById(id: Int): UserInfo
    suspend fun changeUserRating(form: ChangeRatingDto): String
    suspend fun addNote(form: AddNoteDto): String
    suspend fun addComplaint(form: AddComplaintDto): String
}