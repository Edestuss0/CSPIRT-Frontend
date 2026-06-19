package com.cpirt.app.core.domain.session.dto

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

val IS_AUTHORIZED = booleanPreferencesKey("is_authorized")
val ID = intPreferencesKey("id")