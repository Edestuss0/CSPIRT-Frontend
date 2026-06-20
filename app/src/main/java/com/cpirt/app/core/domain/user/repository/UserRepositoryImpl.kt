package com.cpirt.app.core.domain.user.repository

import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.domain.cache.user.repository.IUserCacheRepository
import com.cpirt.app.core.domain.user.dto.AddComplaintDto
import com.cpirt.app.core.domain.user.dto.AddNoteDto
import com.cpirt.app.core.domain.user.dto.ChangeRatingDto
import com.cpirt.app.core.domain.user.dto.UserResult
import com.cpirt.app.entities.UserInfo
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val userCache: IUserCacheRepository
) : IUserRepository {
    val client = apiClient.client

    override suspend fun getUserById(id: Int, force: Boolean): Flow<UserResult> = flow {
        val cached = userCache.get(id)
        if (cached != null && !force) {
            emit(UserResult.Success(cached))
        } else {
            emit(UserResult.Loading)
        }
        try {
            val response = client.get("$API_URL/api/users?id=$id")
            if (response.status.isSuccess()) {
                val body = response.body<UserInfo>()
                userCache.insert(body, isMe = false)
                emit(UserResult.Success(data = body))
            } else {
                emit(UserResult.Error("Ошибка при попытке получения профила", cached))
            }
        } catch (e: Exception) {
            emit(UserResult.Error("Нет соединения с сервером", cached))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMe(force: Boolean): Flow<UserResult> = flow {
        val cached = userCache.getMe()
        if (cached != null && !force) {
            emit(UserResult.Success(cached))
        } else {
            emit(UserResult.Loading)
        }
        try {
            val response = client.get("${API_URL}/api/me")
            if (response.status.isSuccess()) {
                val body = response.body<UserInfo>()
                userCache.insert(body, isMe = true)
                emit(UserResult.Success(body))
            } else {
                emit(UserResult.Error(message = "Ошибка при попытке получения профила", oldData = cached))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(UserResult.Error("Нет соединения с сервером", cached))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changeUserRating(form: ChangeRatingDto): String {
        val response = client.patch("$API_URL/api/rating/update") {
            contentType(ContentType.Application.Json)
            setBody(form)
        }

        if (response.status.isSuccess()) {
            return "Рейтинг успешно изменён"
        }

        throw IOException("Ошибка при изменения рейтинга")
    }

    override suspend fun addNote(form: AddNoteDto): String {
        val response = client.patch("$API_URL/api/note/add") {
            contentType(ContentType.Application.Json)
            setBody(form)
        }

        if (response.status.isSuccess()) {
            userCache.invalidateUser(form.targetId)
            return "Заметка успешно добавлена"
        }

        throw IOException("Ошибка при попытке добавления заметки")
    }

    override suspend fun addComplaint(form: AddComplaintDto): String {
        val response = client.patch("$API_URL/api/complaint/add") {
            contentType(ContentType.Application.Json)
            setBody(form)
        }

        if (response.status.isSuccess()) {
            userCache.invalidateUser(form.targetId)
            return "Жалоба успешно добавлена"
        }

        throw IOException("Ошибка при попытке добавления Жалобы")
    }
}