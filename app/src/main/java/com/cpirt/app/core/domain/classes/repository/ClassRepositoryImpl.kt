package com.cpirt.app.core.domain.classes.repository

import android.util.Log
import com.cpirt.app.core.api.ApiClient
import com.cpirt.app.core.app.API_URL
import com.cpirt.app.core.domain.cache.classes.repository.IClassCacheRepository
import com.cpirt.app.core.domain.cache.parallels.repository.IParallelsCacheRepository
import com.cpirt.app.core.domain.cache.user.repository.IUserCacheRepository
import com.cpirt.app.core.domain.classes.dto.ClassResult
import com.cpirt.app.core.domain.classes.dto.ClassesResponseDto
import com.cpirt.app.core.domain.classes.dto.ClassesResult
import com.cpirt.app.core.domain.classes.dto.ParallelDto
import com.cpirt.app.core.domain.classes.dto.ParallelResult
import com.cpirt.app.core.domain.classes.dto.ParallelsResponseDto
import com.cpirt.app.core.domain.classes.dto.ParallelsResult
import com.cpirt.app.entities.Parallel
import com.cpirt.app.entities.UserRole
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ClassRepositoryImpl @Inject constructor(
    private val apiClient: ApiClient,
    private val userCacheRepository: IUserCacheRepository,
    private val classCacheRepository: IClassCacheRepository,
    private val parallelCacheRepository: IParallelsCacheRepository
) : IClassRepository {
    val client = apiClient.client

    override suspend fun getMyClass(force: Boolean): Flow<ClassResult> = flow {
        val id = userCacheRepository.getMe()?.user?.classId ?: -1
        val cachedClass = classCacheRepository.get(id)
        if (cachedClass != null && !force) {
            emit(ClassResult.Success(data = cachedClass))
        } else {
            emit(ClassResult.Loading)
        }

        try {
            val response = client.get("$API_URL/api/classes?class_id=$id")
            if (response.status.isSuccess()) {
                val body = response.body<ClassesResponseDto>().schoolClasses.firstOrNull()
                    ?: throw IOException("Ошибка при попытке получения класса")
                val members = body.members.filter { it.role == UserRole.User || it.role == UserRole.Helper }
                val finalClass = body.copy(members = members)
                classCacheRepository.insert(finalClass)
                emit(ClassResult.Success(
                    data = finalClass,
                ))
            } else {
                emit(ClassResult.Error(
                    message = "Не удалось получить данные с сервера",
                    data = cachedClass
                ))
            }
        } catch (e: Exception) {

            if (e is CancellationException) throw e

            emit(ClassResult.Error(
                message = "Нет соединения с сервером",
                data = cachedClass
            ))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getClass(id: Int, force: Boolean): Flow<ClassResult> = flow {
        val cachedClass = classCacheRepository.get(id)
        if (cachedClass != null && !force) {
            emit(ClassResult.Success(data = cachedClass))
        } else {
            emit(ClassResult.Loading)
        }

        try {
            val response = client.get("$API_URL/api/classes?class_id=$id")
            if (response.status.isSuccess()) {
                val body = response.body<ClassesResponseDto>().schoolClasses.firstOrNull()
                    ?: throw IOException("Ошибка при попытке получения класса")
                val members = body.members.filter { it.role == UserRole.User || it.role == UserRole.Helper }
                val finalClass = body.copy(members = members)
                classCacheRepository.insert(finalClass)
                emit(ClassResult.Success(
                    data = finalClass,
                ))
            } else {
                emit(ClassResult.Error(
                    message = "Не удалось получить данные с сервера",
                    data = cachedClass
                ))
            }
        } catch (e: Exception) {

            if (e is CancellationException) throw e

            emit(ClassResult.Error(
                message = "Нет соединения с сервером",
                data = cachedClass
            ))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getParallelClasses(id: Int, force: Boolean): Flow<ClassesResult> = flow {
        val ids = getParallel(id).filterIsInstance<ParallelResult.Success>().firstOrNull()?.data?.classes
        val cached = if (ids != null) { classCacheRepository.getClasses(ids) } else null

        if (cached != null && !force) {
            emit(ClassesResult.Success(cached))
        } else {
            emit(ClassesResult.Loading)
        }

        try {
            val response = client.get("$API_URL/api/classes/parallel/$id/classes")
            if (response.status.isSuccess()) {
                val body = response.body<ClassesResponseDto>().schoolClasses
                val finalClasses = body.map { current ->
                    val members = current.members.filter { it.role == UserRole.User || it.role == UserRole.Helper }
                    current.copy(members = members)
                }
                classCacheRepository.insertСlasses(finalClasses)
                emit(ClassesResult.Success(
                    data = finalClasses,
                ))
            } else {
                emit(ClassesResult.Error(
                    message = "Не удалось получить данные с сервера",
                    data = null
                ))
            }
        } catch (e: Exception) {

            if (e is CancellationException) throw e

            emit(ClassesResult.Error(
                message = "Нет соединения с сервером",
                data = null
            ))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getParallels(force: Boolean): Flow<ParallelsResult> = flow {
        val cached = parallelCacheRepository.getAll()

        if (cached != null && !force) {
            emit(ParallelsResult.Success(cached))
        } else {
            emit(ParallelsResult.Loading)
        }

        try {
            val response = client.get("$API_URL/api/classes/parallel")
            if (response.status.isSuccess()) {
                val parallels = response.body<ParallelsResponseDto>().parallels
                val finalParallels = parallels.map { parallel ->
                    val classes = try {
                        getParallelClasses(parallel.id).filterIsInstance<ClassesResult.Success>().firstOrNull()
                    } catch (_: Exception) {
                        null
                    }

                    Parallel(
                        id = parallel.id,
                        bestClass = classes?.data?.find { it.id == parallel.bestClassId },
                        classes = classes?.data,
                        name = parallel.name
                    )
                }
                finalParallels.forEach {
                    parallelCacheRepository.insert(it)
                }
                emit(ParallelsResult.Success(
                    data = finalParallels
                ))
            } else {
                emit(ParallelsResult.Error(
                    message = "Не удалось получить данные с сервера",
                    data = null
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ParallelsResult.Error(
                message = "Нет соединения с сервером",
                data = null
            ))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getParallel(id: Int, force: Boolean): Flow<ParallelResult> = flow {
        val raw = parallelCacheRepository.get(id)
        val cached = if (raw != null) {
            ParallelDto(
                name = raw.name,
                id = raw.id,
                bestClassId = raw.bestClass!!.id,
                classes = raw.classes!!.map { it.id }
            )
        } else null

        if (cached != null && !force) {
            emit(ParallelResult.Success(cached))
        } else {
            emit(ParallelResult.Loading)
        }

        try {
            val response = client.get("$API_URL/api/classes/parallel/$id")
            if (response.status.isSuccess()) {
                val parallel = response.body<ParallelDto>()
                emit(ParallelResult.Success(
                    data = parallel
                ))
            } else {
                emit(ParallelResult.Error(
                    message = "Не удалось получить данные с сервера",
                    data = cached
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ParallelResult.Error(
                message = "Нет соединения с сервером",
                data = cached
            ))
        }
    }.flowOn(Dispatchers.IO)
}