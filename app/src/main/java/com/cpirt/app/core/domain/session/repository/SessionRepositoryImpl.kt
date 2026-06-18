package com.cpirt.app.core.domain.session.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.cpirt.app.core.domain.session.dto.IS_AUTHORIZED
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

@Singleton
class SessionRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) :  ISessionRepository {
    private val datastore = context.applicationContext.dataStore

    override fun getAuthorizedStatus(): Flow<Boolean?> {
        return datastore.data.map { preferences ->
            preferences[IS_AUTHORIZED]
        }
    }

    override suspend fun authorize() {
        datastore.edit { preferences ->
            preferences[IS_AUTHORIZED] = true
        }
    }

    override suspend fun logout() {
        datastore.edit { preferences ->
            preferences.remove(IS_AUTHORIZED)
        }
    }
}