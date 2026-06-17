package com.cpirt.app.core.domain.my_class.repository

import android.util.Log
import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.domain.my_class.dto.ClassesResponseDto
import com.cpirt.app.core.domain.profile.repository.IProfileRepository
import com.cpirt.app.entities.SchoolClass
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
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

        Log.d("MY_CLASS", response.bodyAsText())

        if (response.status.isSuccess()) {
            val body = response.body<ClassesResponseDto>().schoolClasses.firstOrNull()
                ?: throw IOException("Ошибка при попытке получения класса")
            return body
        }

        throw IOException("Ошибка при попытке получения класса")
    }
}