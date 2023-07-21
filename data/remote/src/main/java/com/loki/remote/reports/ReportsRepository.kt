package com.loki.remote.reports

import android.net.Uri
import com.loki.remote.util.Resource
import com.loki.remote.model.MatchedReport
import com.loki.remote.model.Profile
import com.loki.remote.model.Report
import kotlinx.coroutines.flow.Flow

interface ReportsRepository {

    val profiles: List<Profile>?

    suspend fun getReports(): Flow<Resource<List<MatchedReport>>>

    suspend fun addReport(imageUri: Uri?, report: Report)

    suspend fun getReport(reportId: String): MatchedReport

    suspend fun editReport(report: Report)

    suspend fun deleteReport()

    suspend fun getProfiles()

    fun getReportId(): String
}