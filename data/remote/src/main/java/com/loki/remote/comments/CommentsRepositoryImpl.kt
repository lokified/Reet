package com.loki.remote.comments

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.loki.remote.util.Resource
import com.loki.remote.model.Comment
import com.loki.remote.model.MatchedComment
import com.loki.remote.reports.ReportsRepository
import com.loki.remote.util.trace
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentsRepositoryImpl @Inject constructor(
    private val storage: FirebaseFirestore,
    private val reportsRepository: ReportsRepository
): CommentsRepository {

    override suspend fun getComments(reportId: String): Flow<Resource<List<MatchedComment>>> = callbackFlow {

        trySend(Resource.Loading())

        val subscription = storage.collection(REPORTS_COLLECTION)
            .document(COMMENT)
            .collection(COMMENTS_COLLECTION)
            .whereEqualTo(REPORT_ID_FIELD, reportId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                error?.let { e ->
                    trySend(Resource.Error(e.message.toString()))
                    cancel(e.message.toString())
                }

                snapshot?.let {

                    if (!it.isEmpty) {

                        val comments = it.toObjects(Comment::class.java)

                        val matchedComments = comments.mapNotNull { comment ->
                            reportsRepository.profiles?.find { profile ->
                                profile.userId == comment.userId }?.let { matchedProfile ->
                                MatchedComment(matchedProfile, comment)
                            }
                        }

                        trySend(
                            Resource.Success(matchedComments)
                        )
                    }
                }
            }

        awaitClose { subscription.remove() }
    }

    override suspend fun addComment(comment: Comment) {
        trace(ADD_COMMENT_TRACE) {
            storage.collection(REPORTS_COLLECTION)
                .document(COMMENT)
                .collection(COMMENTS_COLLECTION)
                .add(comment)
                .await()
        }
    }

    override suspend fun editComment(comment: Comment) {
        trace(UPDATE_COMMENT_TRACE) {
            storage.collection(REPORTS_COLLECTION)
                .document(COMMENT)
                .collection(COMMENTS_COLLECTION)
                .document(comment.id)
                .set(comment)
                .await()
        }
    }

    override suspend fun deleteComment(commentId: String) {
        trace(DELETE_COMMENT_TRACE) {
            storage.collection(REPORTS_COLLECTION)
                .document(COMMENT)
                .collection(COMMENTS_COLLECTION)
                .document(commentId)
                .delete()
                .await()
        }
    }

    companion object {

        //collections
        const val REPORTS_COLLECTION = "report_collections"
        const val COMMENT = "comment"
        const val COMMENTS_COLLECTION = "comments_collections"
        const val REPORT_ID_FIELD = "reportId"

        //traces
        const val ADD_COMMENT_TRACE = "addCommentTrace"
        const val UPDATE_COMMENT_TRACE = "updateCommentTrace"
        const val DELETE_COMMENT_TRACE = "deleteCommentTrace"
    }
}