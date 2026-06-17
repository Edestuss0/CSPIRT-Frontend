package com.cpirt.app.core.domain.profile.repository

import android.util.Log
import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.entities.UserInfo
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient
) : IProfileRepository {
    val client = apiClient.client

    private val _meData = MutableStateFlow<UserInfo?>(null)

    override suspend fun getMe(): UserInfo {
        val response = client.get("${API_URL}/api/me")

        Log.d("PROFILE", response.bodyAsText())

        if (response.status.isSuccess()) {
            val body = response.body<UserInfo>()
            _meData.update { body }
            return body
        }

        throw kotlinx.io.IOException("Ошибка при попытке получения профиля пользователя")
    }

    override fun getMeData(): StateFlow<UserInfo?> {
        return _meData.asStateFlow()
    }
}