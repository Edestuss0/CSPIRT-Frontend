package com.cpirt.app.core.utils

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toDate(): String? {
    val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.parse(this.substringBefore("T"))
    } else {
        return null
    }
    return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}