package com.loki.report

import com.loki.remote.model.MatchedComment
import com.loki.remote.model.MatchedReport

data class ReportUiState(
    val commentContent: String = "",
    val matchedReport: MatchedReport? = null
)

data class CommentState(
    val isLoading: Boolean = false,
    val matchedComment: List<MatchedComment> = emptyList(),
    val errorMessage: String = ""
)
