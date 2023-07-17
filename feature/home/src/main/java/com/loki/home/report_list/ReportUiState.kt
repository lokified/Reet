package com.loki.home.report_list

import com.loki.remote.model.MatchedReport

data class ReportUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val matchedReport: List<MatchedReport> = emptyList()
)
