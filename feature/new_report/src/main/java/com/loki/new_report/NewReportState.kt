package com.loki.new_report

import android.net.Uri

data class NewReportState(
    val reportContent: String = "",
    val imageUri: Uri? = null
)
