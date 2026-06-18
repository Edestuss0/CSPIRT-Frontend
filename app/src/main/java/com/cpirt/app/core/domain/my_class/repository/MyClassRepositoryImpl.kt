package com.cpirt.app.core.domain.my_class.repository

import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.domain.my_class.dto.ClassesResponseDto
import com.cpirt.app.core.domain.profile.repository.IProfileRepository
import com.cpirt.app.entities.SchoolClass
import com.cpirt.app.entities.UserRole
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import javax.inject.Inject

class MyClassRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val profileRepository: IProfileRepository
) : IMyClassRepository {
    val client = apiClient.client

    override suspend fun getMyClass(): SchoolClass {
        val id = profileRepository.getMe().user.classId
        val response = client.get("$API_URL/api/classes?class_id=$id")
        if (response.status.isSuccess()) {
            val body = response.body<ClassesResponseDto>().schoolClasses.firstOrNull()
                ?: throw IOException("Ошибка при попытке получения класса")
            val members = body.members.filter { it.role == UserRole.User || it.role == UserRole.Helper }
            val finalClass = body.copy(members = members)
            return finalClass
        }

        throw IOException("Ошибка при попытке получения класса")
    }
}