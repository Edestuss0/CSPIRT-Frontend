package com.cpirt.app.data.features.classes.remote.source

import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.data.api.ApiClient
import com.cpirt.app.data.features.classes.remote.dto.ClassesResponseDto
import com.cpirt.app.domain.classes.entity.SchoolClass
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

class ClassRemoteSource @Inject constructor(
    private val apiClient: ApiClient
){
    val client = apiClient.client

    suspend fun getClass(id: Int): SchoolClass {
        val response = client.get("${API_URL}/api/classes?class_id=$id")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
        return response.body<ClassesResponseDto>().schoolClasses.firstOrNull()?.toDomain()
            ?: throw IllegalArgumentException("Ошибка валидации")
    }

    suspend fun getParallelClasses(id: Int): List<SchoolClass> {
        val response = client.get("${API_URL}/api/classes/parallel/$id/classes")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
        return response.body<ClassesResponseDto>().schoolClasses.map { it.toDomain() }
            .sortedByDescending {(it.classTotalRating + it.userTotalRating)}
    }

}