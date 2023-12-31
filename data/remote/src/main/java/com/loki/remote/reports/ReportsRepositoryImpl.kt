package com.loki.remote.reports

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.storage.FirebaseStorage
import com.loki.remote.util.Resource
import com.loki.remote.auth.AuthRepository
import com.loki.remote.comments.CommentsRepositoryImpl
import com.loki.remote.model.Comment
import com.loki.remote.model.MatchedReport
import com.loki.remote.model.Profile
import com.loki.remote.model.Report
import com.loki.remote.profiles.ProfilesRepository
import com.loki.remote.util.trace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
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


    override val profiles: List<Profile>?
        get() = profileList.value

    override suspend fun getReports(): Flow<Resource<List<MatchedReport>>> = callbackFlow {

        launch(Dispatchers.IO) {
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

                    launch(Dispatchers.IO) {

                        if (!it.isEmpty) {

                            val reports = it.toObjects(Report::class.java)

                            val matchedReports = reports.mapNotNull { report ->

                                val numberOfComments = getNumberOfComments(report.id).first()

                                profileList.value?.find { profile -> profile.userId == report.userId }?.let { matchedProfile ->
                                    MatchedReport(report, matchedProfile, numberOfComments)
                                }
                            }

                            trySend(
                                Resource.Success(matchedReports)
                            )
                        }
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

    override suspend fun getReport(reportId: String): MatchedReport {
        trace(GET_REPORT_TRACE) {

            reportIds.value = reportId

            val numberOfComments = getNumberOfComments(reportId).first()

            val report = storage.collection(REPORTS_COLLECTION)
                .document(reportId)
                .get().await().toObject(Report::class.java)

            val matchedProfile = profileList.value?.find { profile ->
                profile.userId == report?.userId
            }

            return MatchedReport(
                report = report!!,
                profile = matchedProfile!!,
                numberOfComments = numberOfComments
            )
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

            // delete report item
            storage.collection(REPORTS_COLLECTION)
                .document(reportIds.value)
                .delete().await()

            // also delete its comments
            val commentsListFlow  = storage.collection(CommentsRepositoryImpl.REPORTS_COLLECTION)
                .document(CommentsRepositoryImpl.COMMENT)
                .collection(CommentsRepositoryImpl.COMMENTS_COLLECTION)
                .whereEqualTo(CommentsRepositoryImpl.REPORT_ID_FIELD, reportIds.value)
                .dataObjects<Comment>()

            val comments = commentsListFlow.first()

            for (comment in comments) {
                storage.collection(CommentsRepositoryImpl.REPORTS_COLLECTION)
                    .document(CommentsRepositoryImpl.COMMENT)
                    .collection(CommentsRepositoryImpl.COMMENTS_COLLECTION)
                    .document(comment.id)
                    .delete()
                    .await()
            }
        }
    }

    override fun getReportId(): String = reportIds.value

    override suspend fun getProfiles() {
        profilesRepository.getProfiles().collect { results ->
            profileList.value = results
        }
    }

    private suspend fun addImageToFirebaseStorage(imageUri: Uri, imageName: String): Uri {

        return store.reference.child(REPORT_IMAGE_REFERENCE)
            .child(imageName)
            .putFile(imageUri).await()
            .storage.downloadUrl.await()
    }

    private suspend fun getNumberOfComments(reportId: String): Flow<Int> = callbackFlow {

        val subscription = storage.collection(REPORTS_COLLECTION)
            .document(CommentsRepositoryImpl.COMMENT)
            .collection(CommentsRepositoryImpl.COMMENTS_COLLECTION)
            .whereEqualTo(CommentsRepositoryImpl.REPORT_ID_FIELD, reportId)
            .addSnapshotListener { snapshot, _ ->

                snapshot?.let {
                    trySend(it.size())
                }
            }
        awaitClose { subscription.remove() }
    }

    companion object {

        private var reportIds = mutableStateOf("")
        var profileList = mutableStateOf<List<Profile>?>(null)

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