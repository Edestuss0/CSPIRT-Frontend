package com.cpirt.app.data.features.classes.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.data.features.classes.local.source.ClassLocalSource
import com.cpirt.app.data.features.classes.local.source.ParallelsLocalSource
import com.cpirt.app.data.features.classes.remote.source.ClassRemoteSource
import com.cpirt.app.data.features.classes.remote.source.ParallelRemoteSource
import com.cpirt.app.domain.classes.entity.Parallel
import com.cpirt.app.domain.classes.entity.SchoolClass
import com.cpirt.app.domain.classes.repository.IClassRepository
import com.cpirt.app.domain.user.entity.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ClassRepositoryImpl @Inject constructor(
    private val remoteClass: ClassRemoteSource,
    private val remoteParallel: ParallelRemoteSource,
    private val classCacheRepository: ClassLocalSource,
    private val parallelCacheRepository: ParallelsLocalSource
) : IClassRepository {

    override suspend fun getClass(id: Int, force: Boolean): Flow<AppResult<SchoolClass>> = flow {
        val cached = classCacheRepository.get(id)
        if (cached != null && !force) {
            emit(AppResult.Success(data = cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
        val response = remoteClass.getClass(id)
        val members = response.members.filter { it.role == UserRole.User || it.role == UserRole.Helper }
            val finalClass = response.copy(members = members)
            classCacheRepository.insert(finalClass)
            emit(
                AppResult.Success(
                    data = finalClass,
                )
            )
        } catch (e: Exception) {

            if (e is CancellationException) throw e

            if (e is IOException) {
                emit(
                    AppResult.Error(
                        message = "Нет соединения с сервером",
                        data = cached
                    )
                )
                return@flow
            }

            emit(AppResult.Error(
                message = "Ошибка при попытке получения класса",
                data = cached
            ))
            return@flow

        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getParallelClasses(id: Int, force: Boolean): Flow<AppResult<List<SchoolClass>>> = flow {
        val ids = parallelCacheRepository.get(id)?.classes
        val cached = if (ids != null) {
            classCacheRepository.getClasses(ids)
        } else null

        if (cached != null && !force) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remoteClass.getParallelClasses(id)
            val finalClasses = response.map { current ->
                val members = current.members.filter { it.role == UserRole.User || it.role == UserRole.Helper }
                current.copy(members = members)
            }
            classCacheRepository.insertСlasses(finalClasses)
            emit(AppResult.Success(finalClasses))
        } catch (e: Exception) {

            if (e is CancellationException) throw e

            if (e is IOException) {
                emit(
                    AppResult.Error(
                        message = "Нет соединения с сервером",
                        data = null
                    )
                )
            }

            emit(
                AppResult.Error(
                    message = "Ошибка при попытке получения классов",
                    data = null
                )
            )
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getParallels(force: Boolean): Flow<AppResult<List<Parallel>>> = flow {
        val cached = parallelCacheRepository.getAll()

        if (cached.isNotEmpty() && !force) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remoteParallel.getParallels()
            val finalParallels = response.map {
                val bestClass = getClass(id = it.bestClassId, force = force).filterIsInstance<AppResult.Success<SchoolClass>>().firstOrNull()
                Parallel(
                    id = it.id,
                    name = it.name,
                    bestClass = bestClass?.data,
                    classes = it.classes
                )
            }
            finalParallels.forEach {
                parallelCacheRepository.insert(it)
            }
            emit(AppResult.Success(finalParallels))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is IOException) {
                emit(
                    AppResult.Error(
                        message = "Нет соединения с сервером",
                        data = null
                    )
                )
            } else {
                emit(
                    AppResult.Error(
                        message = "Ошибка при попытке получения параллелей",
                        data = null
                    )
                )
            }
        }
    }.flowOn(Dispatchers.IO)
}