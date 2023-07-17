package com.loki.remote.reports

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.loki.remote.Resource
import com.loki.remote.auth.AuthRepository
import com.loki.remote.model.MatchedReport
import com.loki.remote.model.Profile
import com.loki.remote.model.Report
import com.loki.remote.profiles.ProfilesRepository
import com.loki.remote.trace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.random.Random

class ReportsRepositoryImpl @Inject constructor(
    private val store: FirebaseStorage,
    private val storage: FirebaseFirestore,
    private val auth: AuthRepository,
    private val profilesRepository: ProfilesRepository
): ReportsRepository {

    override suspend fun getReports(): Flow<Resource<List<MatchedReport>>> = callbackFlow {

        CoroutineScope(Dispatchers.IO).launch {
            getProfiles()
        }
            trySend(Resource.Loading())

            val subscription = storage.collection(REPORTS_COLLECTION)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    error?.let { e ->
                        trySend(Resource.Error(e.message.toString()))
                        cancel(e.message.toString())
                    }



                    snapshot?.let {

                        if (!it.isEmpty) {

                            val reports = it.toObjects(Report::class.java)

                            val matchedReports = reports.mapNotNull { report ->
                                profiles.value?.find { profile -> profile.userId == report.userId }?.let { matchedProfile ->
                                    MatchedReport(report, matchedProfile)
                                }
                            }

                            trySend(
                                Resource.Success(matchedReports)
                            )
                        }
                    }
                }

            awaitClose { subscription.remove() }
        }


    override suspend fun addReport(imageUri: Uri?, report: Report) {
        trace(ADD_REPORT_TRACE) {

            if (imageUri == null) {
                val reportWithId = report.copy(userId = auth.currentUserId)
                storage.collection(REPORTS_COLLECTION)
                    .add(reportWithId)
                    .await()
            }
            else {
                val imageName = "${Random(100).nextInt()}-${report.createdAt}-${report.createdOn}"
                val url = addImageToFirebaseStorage(imageUri, imageName)

                val reportWithImage = report.copy(userId = auth.currentUserId, reportImage = url.toString())

                storage.collection(REPORTS_COLLECTION)
                    .add(reportWithImage)
                    .await()
            }
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

    override suspend fun getProfiles() {
        profilesRepository.getProfiles().collect { results ->
            profiles.value = results
        }
    }

    private suspend fun addImageToFirebaseStorage(imageUri: Uri, imageName: String): Uri {

        return store.reference.child(REPORT_IMAGE_REFERENCE)
            .child(imageName)
            .putFile(imageUri).await()
            .storage.downloadUrl.await()
    }

    companion object {

        val reportIds = mutableStateOf("")
        var profiles = mutableStateOf<List<Profile>?>(null)

        //collections
        const val REPORTS_COLLECTION = "report_collections"
        const val USER_ID_FIELD = "userId"

        //reference
        const val REPORT_IMAGE_REFERENCE = "report_images"

        //traces
        const val ADD_REPORT_TRACE = "addReportTrace"
        const val UPDATE_REPORT_TRACE = "updateReportTrace"
        const val GET_REPORT_TRACE = "getReportTrace"
        const val DELETE_REPORT_TRACE = "deleteReportTrace"
    }
}