package com.loki.new_report

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.FirebaseException
import com.loki.local.datastore.DataStoreStorage
import com.loki.remote.model.Report
import com.loki.remote.reports.ReportsRepository
import com.loki.ui.utils.DateUtil
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewReportViewModel @Inject constructor(
    dataStore: DataStoreStorage,
    private val storage: ReportsRepository
): ReetViewModel(dataStore) {

    var state = mutableStateOf(NewReportState())
        private set

    private val reportContent
        get() = state.value.reportContent

    private val imageUri
        get() = state.value.imageUri

    init {
        getUser()
        getLocalProfile()
    }

    fun onChangeReportContent(newValue: String) {
        state.value = state.value.copy(reportContent = newValue)
    }

    fun onChangeImageUri(newValue: Uri) {
        state.value = state.value.copy(imageUri = newValue)
    }

    fun addReport(navigateToHome: () -> Unit) {

        launchCatching {
            try {
                isLoading.value = true
                storage.addReport(
                    report = Report(
                        reportContent = reportContent,
                        createdAt = System.currentTimeMillis(),
                        createdOn = DateUtil.getCurrentDate(),
                        userId = localUser.value.userId
                    ),
                    imageUri = imageUri
                )
                isLoading.value = false
                navigateToHome()
                resetField()
            } catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "Something went wrong"
            }
        }
    }

    private fun resetField() {
        state.value = NewReportState()
    }
}