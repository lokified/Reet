package com.loki.home.report_list

import com.google.firebase.FirebaseException
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalUser
import com.loki.remote.model.Report
import com.loki.remote.reports.ReportsRepository
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val dataStore: DataStoreStorage,
    private val reportRepository: ReportsRepository
): ReetViewModel(dataStore) {

    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports = _reports.asStateFlow()

    init {
        getUser()
        getReports()
    }

    private fun getReports() {
        launchCatching {
            try {
                isLoading.value = true
                reportRepository.reports.collect {
                    _reports.value = it
                }
                isLoading.value = false
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "Something went wrong"
            }
        }
    }
}