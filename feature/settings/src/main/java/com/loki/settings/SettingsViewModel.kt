package com.loki.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.local.datastore.DataStoreStorage
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreStorage: DataStoreStorage
): ReetViewModel(dataStoreStorage) {

    fun changeAppTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            dataStoreStorage.saveAppTheme(isDarkTheme)
        }
    }
}