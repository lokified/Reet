package com.loki.report

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.FirebaseException
import com.loki.local.datastore.DataStoreStorage
import com.loki.remote.util.Resource
import com.loki.remote.comments.CommentsRepository
import com.loki.remote.model.Comment
import com.loki.remote.reports.ReportsRepository
import com.loki.ui.utils.Constants.REPORT_ID
import com.loki.ui.utils.DateUtil
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    dataStore: DataStoreStorage,
    savedStateHandle: SavedStateHandle,
    private val reports: ReportsRepository,
    private val comments: CommentsRepository
): ReetViewModel(dataStore) {

    var state = mutableStateOf(ReportUiState())
        private set

    private val commentContent
        get() = state.value.commentContent

    private val matchedReport
        get() = state.value.matchedReport

    private val _commentState = MutableStateFlow(CommentState())
    val commentState = _commentState.asStateFlow()

    init {
        getLocalProfile()
        getUser()
        savedStateHandle.get<String>(REPORT_ID)?.let { reportId ->
            getReport(reportId)
            getComments(reportId)
        }
    }

    fun onCommentChange(newValue: String) {
        state.value = state.value.copy(commentContent = newValue)
    }

    private fun getReport(reportId: String) {
        launchCatching {
            try {
                isLoading.value = true

                state.value = state.value.copy(
                    matchedReport = reports.getReport(reportId)
                )

                isLoading.value = false
            } catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "Something went wrong"
            }
        }
    }

    private fun getComments(reportId: String) {
        launchCatching {
            comments.getComments(reportId).collect { result ->

                when(result) {
                    is Resource.Loading -> {
                        _commentState.value = CommentState(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _commentState.value = CommentState(
                            matchedComment = result.data
                        )
                    }
                    is Resource.Error -> {
                        _commentState.value = CommentState(
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }

    fun addComment() {
        launchCatching {
            try {
                isLoading.value = true

                comments.addComment(
                    Comment(
                        commentContent = commentContent,
                        createdAt = System.currentTimeMillis(),
                        createdOn = DateUtil.getCurrentDate(),
                        userId = localUser.value.userId,
                        reportId = matchedReport!!.report.id
                    )
                )

                isLoading.value = false
                resetField()
            } catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "Something went wrong"
            }
        }
    }

    private fun resetField() {
        state.value = state.value.copy(commentContent = "")
    }
}