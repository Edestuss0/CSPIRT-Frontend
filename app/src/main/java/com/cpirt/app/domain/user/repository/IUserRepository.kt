package com.cpirt.app.domain.user.repository

import com.cpirt.app.domain.user.entity.LoginForm
import com.cpirt.app.domain.user.entity.AddComplaintForm
import com.cpirt.app.domain.user.entity.AddNoteForm
import com.cpirt.app.domain.user.entity.ChangeRatingForm
import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.domain.user.entity.UserInfo
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUserById(id: Int, force: Boolean = false): Flow<AppResult<UserInfo>>
    suspend fun changeUserRating(form: ChangeRatingForm): String
    suspend fun addNote(form: AddNoteForm): String
    suspend fun addComplaint(form: AddComplaintForm): String
    suspend fun getMe(force: Boolean): Flow<AppResult<UserInfo>>

    suspend fun login(form: LoginForm): String
    suspend fun logout()
    fun getAuthorizedStatus(): Flow<Boolean?>
}