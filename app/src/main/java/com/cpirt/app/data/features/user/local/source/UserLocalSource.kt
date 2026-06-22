package com.cpirt.app.data.features.user.local.source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.cpirt.app.data.features.user.local.dao.UserCacheDao
import com.cpirt.app.data.features.user.local.entity.ID
import com.cpirt.app.data.features.user.local.entity.IS_AUTHORIZED
import com.cpirt.app.data.features.user.local.entity.UserCacheEntity
import com.cpirt.app.domain.user.entity.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

class UserLocalSource @Inject constructor(
    private val dao: UserCacheDao,
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val CACHE_TTL_MS = 72 * 60 * 60 * 1000L
    }
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")
    private val datastore = context.applicationContext.dataStore

    suspend fun get(id: Int): UserInfo? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        return dao.getUser(id)?.let { entity ->
            runCatching { Json.decodeFromString<UserInfo>(entity.json) }.getOrNull()
        }
    }

     suspend fun insert(user: UserInfo, isMe: Boolean) {
        dao.insertUser(
            UserCacheEntity(
                userId = user.user.id,
                json = Json.encodeToString(user),
                cachedAt = System.currentTimeMillis(),
                isMe = isMe
            )
        )
    }

    suspend fun invalidateUser(id: Int) {
        dao.invalidateUser(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }

    suspend fun getMe(): UserInfo? {
        dao.clearExpired(System.currentTimeMillis() - CACHE_TTL_MS)
        return dao.getMe()?.let { entity ->
            runCatching { Json.decodeFromString<UserInfo>(entity.json) }.getOrNull()
        }
    }

    fun getAuthorizedStatus(): Flow<Boolean?> {
        return datastore.data.map { preferences ->
            preferences[IS_AUTHORIZED]
        }
    }
    suspend fun authorize(id: Int) {
        datastore.edit { preferences ->
            preferences[IS_AUTHORIZED] = true
            preferences[ID] = id
        }
    }

    suspend fun logout() {
        datastore.edit { preferences ->
            preferences.remove(IS_AUTHORIZED)
            preferences.remove(ID)
        }
    }
}