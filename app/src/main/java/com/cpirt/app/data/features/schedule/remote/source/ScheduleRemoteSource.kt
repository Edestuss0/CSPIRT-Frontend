package com.cpirt.app.data.features.schedule.remote.source

import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.data.api.ApiClient
import com.cpirt.app.data.features.schedule.remote.dto.ScheduleResponseDto
import com.cpirt.app.data.features.schedule.remote.dto.toDomain
import com.cpirt.app.domain.schedule.entity.ScheduleLesson
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

class ScheduleRemoteSource @Inject constructor(
    apiClient: ApiClient
) {
    val client = apiClient.client

    suspend fun getScheduleByClassId(id: Int): List<ScheduleLesson> {
        val response = client.get("$API_URL/api/schedules?class_id=$id&type=current")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
        return response.body<ScheduleResponseDto>().schedules.map { it.toDomain() }
    }

    suspend fun getScheduleByTeacherId(id: Int): List<ScheduleLesson> {
        val response = client.get("$API_URL/api/schedules/teacher/current?teacher_id=$id")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
        return response.body<ScheduleResponseDto>().schedules.map { it.toDomain() }
    }
}