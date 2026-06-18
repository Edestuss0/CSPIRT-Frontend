package com.cpirt.app.core.utils

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun String.toImageBitmap(): ImageBitmap? {
    return try {
        val cleanBase64 = substringAfter("base64,")
        val bytes = Base64.decode(cleanBase64, Base64.DEFAULT)

        BitmapFactory.decodeByteArray(
            bytes,
            0,
            bytes.size
        )?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}