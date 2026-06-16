package com.cpirt.app.core.data.auth

import android.util.Log
import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.data.user.UserRepository
import com.cpirt.app.entities.UserInfo
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class AuthorizationRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val userRepository: UserRepository
) {
    val client = apiClient.client

   suspend fun login(form: LoginDto): String {
        val response = client.post("${API_URL}/login") {
            contentType(ContentType.Application.Json)
            setBody(form)
        }

        if (response.status.isSuccess()) {
            userRepository.authorize()
            return "Вход успешен"
        }

        if (response.status.value in 400..499) {
            throw kotlinx.io.IOException("Неправильный логин или пароль")
        }

        throw kotlinx.io.IOException("Непредвиденная ошибка сервера")
    }

    suspend fun logout() {
        val response = client.patch("${API_URL}/api/user/logout")

        if (response.status.isSuccess()) {
            userRepository.logout()
            return
        }

        throw kotlinx.io.IOException("Непредвиденная ошибка сервера")
    }

    suspend fun getMe(): UserInfo {
        val response = client.get("${API_URL}/api/me")

        Log.d("PROFILE", response.bodyAsText())

        if (response.status.isSuccess()) {
            return response.body<UserInfo>()
        }

        throw kotlinx.io.IOException("Ошибка при попытке получения профиля пользователя")
    }
}