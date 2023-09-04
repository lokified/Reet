package com.loki.ui.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

object DateUtil {

    fun getCurrentDate(): Long? {
        var long: Long? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val date = LocalDate.now()
            long = date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        }

        return long
    }

    fun Long.formatDate(): String =
        SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH).format(this)

    fun Long.formatTime(): String {
        val date = Date(this)
        val dateFormat = SimpleDateFormat(TIME_FORMAT, Locale.ENGLISH)
        return dateFormat.format(date)
    }

    fun getFileName(): String {
        return SimpleDateFormat(FILE_NAME_FORMAT, Locale.ENGLISH).format(System.currentTimeMillis())
    }

    private const val DATE_FORMAT = "EEE, d MMM yyyy"
    private const val TIME_FORMAT = "hh:mm a"
    private const val FILE_NAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
}