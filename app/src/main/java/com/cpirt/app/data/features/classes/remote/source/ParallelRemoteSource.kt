package com.cpirt.app.data.features.classes.remote.source

import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.data.api.ApiClient
import com.cpirt.app.data.features.classes.remote.dto.ParallelDto
import com.cpirt.app.data.features.classes.remote.dto.ParallelsResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import javax.inject.Inject

class ParallelRemoteSource @Inject constructor(
    private val apiClient: ApiClient
) {
    val client = apiClient.client

    suspend fun getParallels(): List<ParallelDto> {
        val response = client.get("${API_URL}/api/classes/parallel")
        if (!response.status.isSuccess()) {
            throw ServerException(response.status.value)
        }
        return response.body<ParallelsResponseDto>().parallels
    }


}