package com.loki.ui.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Locale

object DateUtil {

    fun getCurrentDate(): Long? {
        val long: Long? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.now()
            date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        }

        return long
    }

    fun Long.formatDate(): String =
        SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(this)

    fun Long.formatTime(): String = "ss"

    private const val DATE_FORMAT = "EEE, d MMM yyyy"
    private const val TIME_FORMAT = "EEE, d MMM yyyy"
}