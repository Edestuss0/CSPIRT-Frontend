package com.cpirt.app.core.domain.my_class.repository

import android.util.Log
import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.domain.cache.user.repository.IUserCacheRepository
import com.cpirt.app.core.domain.my_class.dto.ClassesResponseDto
import com.cpirt.app.entities.SchoolClass
import com.cpirt.app.entities.UserRole
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import javax.inject.Inject

class MyClassRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val userCacheRepository: IUserCacheRepository
) : IMyClassRepository {
    val client = apiClient.client

    override suspend fun getMyClass(force: Boolean): SchoolClass {
        val id = userCacheRepository.getMe()?.user?.classId ?: -1
        Log.d("CLASS_ID", id.toString())
        try {
            val response = client.get("$API_URL/api/classes?class_id=$id")
            if (response.status.isSuccess()) {
                val body = response.body<ClassesResponseDto>().schoolClasses.firstOrNull()
                    ?: throw IOException("Ошибка при попытке получения класса")
                val members = body.members.filter { it.role == UserRole.User || it.role == UserRole.Helper }
                val finalClass = body.copy(members = members)
                return finalClass
            }
        } catch (_: Exception) {
            throw IOException("Не удалось соединиться с сервером")
        }

        throw IOException("Ошибка при попытке получения класса")
    }
}