package com.cpirt.app.core.utils

import java.time.Instant
import java.time.format.DateTimeFormatter

fun getCurrentIsoTime(): String {
    return DateTimeFormatter.ISO_INSTANT.format(Instant.now())
}
