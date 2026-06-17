package com.cpirt.app.core.api

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Url
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import io.ktor.http.Cookie
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.collections.filter

class PersistentCookiesStorage @Inject constructor(
    @ApplicationContext context: Context
) : CookiesStorage {
    private val Context.cookieStore: DataStore<Preferences> by preferencesDataStore(name = "cookie_storage")

    private val store = context.applicationContext.cookieStore
    private val COOKIES_KEY = stringPreferencesKey("cookies_json")

    private val memoryCache = ConcurrentHashMap<String, MutableList<Cookie>>()
    private var loaded = false
    private val mutex = Mutex()

    override suspend fun get(requestUrl: Url): List<Cookie> {
        ensureLoaded()
        val host = requestUrl.host
        return memoryCache[host]?.filter { cookie ->
            cookie.expires?.let { it > GMTDate() } ?: true
        } ?: emptyList()
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        ensureLoaded()
        val host = requestUrl.host
        mutex.withLock {
            val list = memoryCache.getOrPut(host) { mutableListOf() }
            list.removeAll { it.name == cookie.name }
            list.add(cookie)
            persist()
        }
    }

    override fun close() {}

    private suspend fun ensureLoaded() {
        if (loaded) return
        mutex.withLock {
            if (loaded) return
            store.data.first()[COOKIES_KEY]?.let { json ->
                json.split("\n").filter { it.isNotBlank() }.forEach { line ->
                    val parts = line.split("|")
                    if (parts.size >= 3) {
                        val host = parts[0]
                        val cookie = Cookie(name = parts[1], value = parts[2],
                            path = parts.getOrNull(3), domain = parts.getOrNull(4))
                        memoryCache.getOrPut(host) { mutableListOf() }.add(cookie)
                    }
                }
            }

            loaded = true
        }
    }

    private suspend fun persist() {
        val serialized = memoryCache.entries.joinToString("\n") {(host, cookies) ->
            cookies.joinToString("\n") {c ->
                "$host|${c.name}|${c.value}|${c.path ?: ""}|${c.domain ?: ""}"
            }
        }
        store.edit { it[COOKIES_KEY] = serialized }
    }

    suspend fun clearAll() {
        mutex.withLock {
            store.edit {
                it.remove(COOKIES_KEY)
            }
        }
    }
}