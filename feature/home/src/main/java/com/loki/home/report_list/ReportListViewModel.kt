package com.loki.home.report_list

import androidx.lifecycle.viewModelScope
import com.loki.local.datastore.DataStoreStorage
import com.loki.remote.Resource
import com.loki.remote.model.MatchedReport
import com.loki.remote.profiles.ProfilesRepository
import com.loki.remote.reports.ReportsRepository
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportListViewModel @Inject constructor(
    private val dataStore: DataStoreStorage,
    private val reportRepository: ReportsRepository
): ReetViewModel(dataStore) {

    private val _reportUiState = MutableStateFlow(ReportUiState())
    val reportsUiState = _reportUiState.asStateFlow()

    init {
        getUser()
        getLocalProfile()
        getReports()
    }

    private fun getReports() {
        launchCatching {
            reportRepository.getReports().collect { results ->

                when(results) {
                    is Resource.Loading -> {
                        _reportUiState.value = ReportUiState(
                            isLoading = true
                        )
                    }

                    is Resource.Success -> {

                        val matchedReports = results.data

                        _reportUiState.value = ReportUiState(
                            matchedReport = matchedReports
                        )
                    }

                    is Resource.Error -> {
                        _reportUiState.value = ReportUiState(
                            errorMessage = results.message
                        )
                    }
                }
            }
        }
    }
}