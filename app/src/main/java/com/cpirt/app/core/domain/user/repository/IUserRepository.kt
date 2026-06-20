package com.cpirt.app.core.domain.user.repository

import com.cpirt.app.core.domain.user.dto.AddComplaintDto
import com.cpirt.app.core.domain.user.dto.AddNoteDto
import com.cpirt.app.core.domain.user.dto.ChangeRatingDto
import com.cpirt.app.core.domain.user.dto.UserResult
import com.cpirt.app.entities.UserInfo
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUserById(id: Int, force: Boolean = false): Flow<UserResult>
    suspend fun changeUserRating(form: ChangeRatingDto): String
    suspend fun addNote(form: AddNoteDto): String
    suspend fun addComplaint(form: AddComplaintDto): String
    suspend fun getMe(force: Boolean): Flow<UserResult>
}