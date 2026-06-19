package com.cpirt.app.core.domain.profile.repository

import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.domain.cache.user.repository.IUserCacheRepository
import com.cpirt.app.core.domain.profile.dto.ProfileResult
import com.cpirt.app.entities.UserInfo
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val userCache: IUserCacheRepository
) : IProfileRepository {
    val client = apiClient.client

    private val _meData = MutableStateFlow<UserInfo?>(null)

    override suspend fun getMe(force: Boolean): Flow<ProfileResult> = flow {

        val cached = userCache.getMe()
        val shouldRefetch = force || !userCache.isCacheValid(cached?.user?.id ?: -1) || cached == null
        if (cached != null && !shouldRefetch) {
            emit(ProfileResult.Success(cached, fromCache = true))
            return@flow
        }


        emit(ProfileResult.Loading)

        try {
            val response = client.get("${API_URL}/api/me")
            if (response.status.isSuccess()) {
                val body = response.body<UserInfo>()
                userCache.insert(body, isMe = true)
                emit(ProfileResult.Success(body, fromCache = false))
            } else {
                emit(ProfileResult.Error("Ошибка при попытке получения профила", cached))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ProfileResult.Error("Нет соединения с сервером", cached))
        }
    }.flowOn(Dispatchers.IO)
}