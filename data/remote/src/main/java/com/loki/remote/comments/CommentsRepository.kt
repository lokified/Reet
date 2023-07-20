package com.loki.remote.comments

import com.loki.remote.Resource
import com.loki.remote.model.Comment
import com.loki.remote.model.MatchedComment
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

    suspend fun getComments(reportId: String): Flow<Resource<List<MatchedComment>>>

    suspend fun addComment(comment: Comment)

    suspend fun editComment(comment: Comment)

    suspend fun deleteComment(commentId: String)
}