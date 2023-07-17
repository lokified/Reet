package com.loki.remote.reports

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.loki.remote.auth.AuthRepository
import com.loki.remote.model.Report
import com.loki.remote.trace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val storage: FirebaseFirestore,
    private val auth: AuthRepository
): ReportsRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val reports: Flow<List<Report>>
        get() = auth.currentUser.flatMapLatest { user ->
            storage.collection(REPORTS_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user.id)
                .dataObjects()
        }

    override suspend fun addReport(report: Report) {
        trace(ADD_REPORT_TRACE) {
            val reportWithId = report.copy(userId = auth.currentUserId)
            storage.collection(REPORTS_COLLECTION)
                .add(reportWithId)
                .await()
        }
    }

    override suspend fun getReport(reportId: String): Report? {
        trace(GET_REPORT_TRACE) {
            reportIds.value = reportId
            return storage.collection(REPORTS_COLLECTION)
                .document(reportId)
                .get().await().toObject()
        }
    }

    override suspend fun editReport(report: Report) {
        trace(UPDATE_REPORT_TRACE) {
            storage.collection(REPORTS_COLLECTION)
                .document(reportIds.value)
                .set(report)
                .await()
        }
    }

    override suspend fun deleteReport() {
        trace(DELETE_REPORT_TRACE) {
            storage.collection(REPORTS_COLLECTION)
                .document(reportIds.value)
                .delete().await()
        }
    }

    override fun getReportId(): String = reportIds.value

    companion object {

        val reportIds = mutableStateOf("")

        //collections
        const val REPORTS_COLLECTION = "reports"
        const val USER_ID_FIELD = "userId"

        //traces
        const val ADD_REPORT_TRACE = "addReportTrace"
        const val UPDATE_REPORT_TRACE = "updateReportTrace"
        const val GET_REPORT_TRACE = "getReportTrace"
        const val DELETE_REPORT_TRACE = "deleteReportTrace"
    }
}