package com.cpirt.app.data.api

import com.cpirt.app.core.app.API_URL
import com.cpirt.app.data.features.user.local.source.UserLocalSource
import com.cpirt.app.domain.user.repository.IUserRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiClient @Inject constructor(
    private val userRepository: UserLocalSource,
    private val cookiesStorage: PersistentCookiesStorage
) {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    explicitNulls = false
                }
            )
        }
//        install(HttpRequestRetry) {
//            maxRetries = 3
//            retryOnServerErrors(maxRetries = 3)
//            exponentialDelay()
//        }
//        install(HttpTimeout) {
//            requestTimeoutMillis = 15_000
//            connectTimeoutMillis = 10_000
//            socketTimeoutMillis = 15_000
//        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            sanitizeHeader { it == HttpHeaders.Authorization }
        }
        install(HttpCookies) {
            storage = cookiesStorage
        }
        expectSuccess = false
    }
    init {
        client.plugin(HttpSend).intercept { request ->
            val originalResponse = execute(request)

            if (
                originalResponse.response.status.value == 401 && !request.url.encodedPath.endsWith("/refresh")) {
                val refresh = client.post("$API_URL/refresh")

                if (refresh.status.isSuccess()) {
                    return@intercept execute(request)
                } else {
                    userRepository.logout()
                }
            }

            originalResponse
        }
    }
}