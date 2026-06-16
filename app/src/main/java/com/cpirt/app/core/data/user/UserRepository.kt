package com.cpirt.app.core.data.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

@Singleton
class UserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val datastore = context.applicationContext.dataStore

//    fun getProfile(): Flow<User?> {
//        return datastore.data.map { preferences ->
//            null
//        }
//    }

    fun getAuthorizedStatus(): Flow<Boolean?> {
        return datastore.data.map { preferences ->
            preferences[IS_AUTHORIZED]
        }
    }

    suspend fun authorize() {
        datastore.edit { preferences ->
            preferences[IS_AUTHORIZED] = true
        }
    }

    suspend fun logout() {
        datastore.edit { preferences ->
            preferences.remove(IS_AUTHORIZED)
        }
    }
}