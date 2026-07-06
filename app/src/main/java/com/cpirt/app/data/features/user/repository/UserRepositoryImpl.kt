package com.cpirt.app.data.features.user.repository

import com.cpirt.app.core.entity.AppResult
import com.cpirt.app.core.exception.ServerException
import com.cpirt.app.data.features.classes.local.source.ClassLocalSource
import com.cpirt.app.data.features.classes.local.source.ParallelsLocalSource
import com.cpirt.app.data.features.user.local.source.UserLocalSource
import com.cpirt.app.data.features.user.remote.source.UserRemoteSource
import com.cpirt.app.domain.user.entity.AddComplaintForm
import com.cpirt.app.domain.user.entity.AddNoteForm
import com.cpirt.app.domain.user.entity.ChangeRatingForm
import com.cpirt.app.domain.user.entity.LoginForm
import com.cpirt.app.domain.user.entity.UserInfo
import com.cpirt.app.domain.user.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val remoteClient: UserRemoteSource,
    private val userCache: UserLocalSource,
    private val classCache: ClassLocalSource,
    private val parallelsCache: ParallelsLocalSource
) : IUserRepository {

    override suspend fun getUserById(id: Int, force: Boolean): Flow<AppResult<UserInfo>> = flow {
        emit(AppResult.Loading)

        val cached = userCache.get(id)

        if (cached != null && !force) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remoteClient.getUserById(id)
            userCache.insert(response, isMe = false)
            emit(AppResult.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is IOException) {
                emit(
                    AppResult.Error(
                        message = "Нет соединения с сервером",
                        data = cached
                    )
                )
            } else {
                emit(
                    AppResult.Error(
                        message = "Ошибка при попытке получения пользователя",
                        data = cached
                    )
                )
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getMe(force: Boolean): Flow<AppResult<UserInfo>> = flow {
        emit(AppResult.Loading)

        val cached = userCache.getMe()

        if (cached != null && !force) {
            emit(AppResult.Success(cached))
        } else {
            emit(AppResult.Loading)
        }

        try {
            val response = remoteClient.getMe()
            userCache.insert(response, isMe = true)
            emit(AppResult.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is IOException) {
                emit(
                    AppResult.Error(
                        message = "Нет соединения с сервером",
                        data = cached
                    )
                )
            } else {
                emit(
                    AppResult.Error(
                        message = "Ошибка при попытке получения профиля",
                        data = cached
                    )
                )
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun changeUserRating(form: ChangeRatingForm, userId: Int): String {
        try {
            remoteClient.changeRating(form)
            userCache.invalidateUser(userId)
            return "Рейтинг успешно изменён"
        } catch (e: Exception) {
            if (e is IOException) {
                throw IOException("Нет соединения с интернетом")
            } else {
                throw IOException("Ошибка при изменения рейтинга")
            }
        }
    }

    override suspend fun addNote(form: AddNoteForm): String {
        try {
            remoteClient.addNote(form)
            userCache.invalidateUser(form.targetId)
            return "Заметка успешно добавлена"
        } catch (e: Exception) {
            if (e is IOException) {
                throw IOException("Нет соединения с интернетом")
            } else {
                throw IOException("Ошибка при попытке добавления заметки")
            }
        }
    }

    override suspend fun deleteNote(id: Int, userId: Int) {
        remoteClient.deleteNote(id)
        userCache.invalidateUser(userId)
    }

    override suspend fun addComplaint(form: AddComplaintForm): String {
        try {
            remoteClient.addComplaint(form)
            userCache.invalidateUser(form.targetId)
            return "Жалоба успешно добавлена"
        } catch (e: Exception) {
            if (e is IOException) {
                throw IOException("Нет соединения с интернетом")
            } else {
                throw IOException("Ошибка при попытке добавления жалобы")
            }
        }
    }

    override suspend fun deleteComplaint(id: Int, userId: Int) {
        remoteClient.deleteComplaint(id)
        userCache.invalidateUser(userId)
    }

    override suspend fun login(form: LoginForm): String {
        remoteClient.login(form)
        val user = remoteClient.getMe()

        try {
            userCache.insert(user, isMe = true)
            userCache.authorize(user.user.id)
            return "Вход успешен"
        } catch (e: Exception) {
            if (e is IOException) {
                throw IOException("Нет соединения с интернетом")
            }
            if (e is ServerException) {
                if (e.code in 400..499) {
                    throw IOException("Неправильный логин или пароль")
                }
            }
            throw IOException("Неправильный логин или пароль")
        }
    }

    override suspend fun logout() {
        try {
            remoteClient.logout()
            userCache.clearAll()
            classCache.clearAll()
            parallelsCache.clearAll()
        } catch (e: Exception) {
            if (e is IOException) {
                throw IOException("Нет соединения с интернетом")
                throw IOException("Ошибка при попытке выхода")
            }
        }
    }

    override fun getAuthorizedStatus(): Flow<Boolean?> {
        return userCache.getAuthorizedStatus()
    }
}