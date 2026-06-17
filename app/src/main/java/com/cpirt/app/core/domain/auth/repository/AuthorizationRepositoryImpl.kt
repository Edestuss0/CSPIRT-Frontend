package com.cpirt.app.core.domain.auth.repository

import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.api.PersistentCookiesStorage
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.domain.user.repository.UserRepository
import com.cpirt.app.core.domain.auth.dto.LoginDto
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val userRepository: UserRepository,
    private val sessionStorage: PersistentCookiesStorage
) : IAuthRepository {
    val client = apiClient.client

   override suspend fun login(form: LoginDto): String {
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

    override suspend fun logout() {
        val response = client.patch("${API_URL}/api/user/logout")

        if (response.status.isSuccess()) {
            sessionStorage.clearAll()
            userRepository.logout()
            return
        }

        throw kotlinx.io.IOException("Непредвиденная ошибка сервера")
    }
}