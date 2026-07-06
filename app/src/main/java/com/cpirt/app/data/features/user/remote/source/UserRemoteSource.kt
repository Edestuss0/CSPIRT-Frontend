package com.cpirt.app.data.features.user.remote.source

import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.data.api.ApiClient
import com.cpirt.app.data.features.user.remote.dto.UserInfoDto
import com.cpirt.app.data.features.user.remote.dto.toDto
import com.cpirt.app.domain.user.entity.AddComplaintForm
import com.cpirt.app.domain.user.entity.AddNoteForm
import com.cpirt.app.domain.user.entity.ChangeRatingForm
import com.cpirt.app.domain.user.entity.LoginForm
import com.cpirt.app.domain.user.entity.UserInfo
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class UserRemoteSource @Inject constructor(
    private val apiClient: ApiClient
) {
    private val client = apiClient.client

    suspend fun getUserById(id: Int): UserInfo {
        val response = client.get("${API_URL}/api/users?id=$id")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
        return response.body<UserInfoDto>().toDomain()
    }

    suspend fun getMe(): UserInfo {
        val response = client.get("${API_URL}/api/me")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
        return response.body<UserInfoDto>().toDomain()
    }

    suspend fun addNote(form: AddNoteForm) {
        val response = client.patch("${API_URL}/api/note/add") {
            contentType(ContentType.Application.Json)
            setBody(form.toDto())
        }
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun addComplaint(form: AddComplaintForm) {
        val response = client.patch("${API_URL}/api/complaint/add") {
            contentType(ContentType.Application.Json)
            setBody(form.toDto())
        }
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun changeRating(form: ChangeRatingForm) {
        val response = client.patch("${API_URL}/api/rating/update") {
            contentType(ContentType.Application.Json)
            setBody(form.toDto())
        }
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun login(form: LoginForm) {
        val response = client.post("${API_URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(form)
        }
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun logout() {
        val response = client.patch("${API_URL}/api/user/logout")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun deleteNote(id: Int) {
        val response = client.delete("$API_URL/api/note/delete/$id")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
    }

    suspend fun deleteComplaint(id: Int) {
        val response = client.delete("$API_URL/api/complaint/delete/$id")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
    }
}