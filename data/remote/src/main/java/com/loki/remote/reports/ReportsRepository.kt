package com.loki.remote.reports

import com.loki.remote.model.Report
import kotlinx.coroutines.flow.Flow

interface ReportsRepository {

    val reports: Flow<List<Report>>

    suspend fun addReport(report: Report)

    suspend fun getReport(reportId: String): Report?

    suspend fun editReport(report: Report)

    suspend fun deleteReport()

    fun getReportId(): String
}