package com.loki.remote.comments

import com.loki.remote.model.Comment
import com.loki.remote.model.Report
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

    val comments: Flow<List<Comment>>

    suspend fun addComment(comment: Comment)

    suspend fun editComment(comment: Comment)

    suspend fun deleteComment(commentId: String)
}