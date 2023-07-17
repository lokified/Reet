package com.loki.remote.comments

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.loki.remote.model.Comment
import com.loki.remote.reports.ReportsRepository
import com.loki.remote.trace
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val storage: FirebaseFirestore,
    private val report: ReportsRepository
): CommentsRepository {

    override val comments: Flow<List<Comment>>
        get() = storage.collection(COMMENTS_COLLECTION)
            .whereEqualTo(REPORT_ID_FIELD, report.getReportId())
            .dataObjects()

    override suspend fun addComment(comment: Comment) {
        trace(ADD_COMMENT_TRACE) {
            val commentWithId = comment.copy(reportId = report.getReportId())
            storage.collection(COMMENTS_COLLECTION)
                .add(commentWithId)
                .await()
        }
    }

    override suspend fun editComment(comment: Comment) {
        trace(UPDATE_COMMENT_TRACE) {
            storage.collection(COMMENTS_COLLECTION)
                .document(comment.id)
                .set(comment)
                .await()
        }
    }

    override suspend fun deleteComment(commentId: String) {
        trace(DELETE_COMMENT_TRACE) {
            storage.collection(COMMENTS_COLLECTION)
                .document(commentId)
                .delete()
                .await()
        }
    }

    companion object {

        //collections
        const val COMMENTS_COLLECTION = "reports"
        const val REPORT_ID_FIELD = "reportId"

        //traces
        const val ADD_COMMENT_TRACE = "addCommentTrace"
        const val UPDATE_COMMENT_TRACE = "updateCommentTrace"
        const val DELETE_COMMENT_TRACE = "deleteCommentTrace"
    }
}