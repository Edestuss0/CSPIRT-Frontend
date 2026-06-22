package com.cpirt.app.data.features.user.local.entity

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

val IS_AUTHORIZED = booleanPreferencesKey("is_authorized")
val ID = intPreferencesKey("id")