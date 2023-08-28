package com.loki.reet

import android.app.Application
import com.loki.local.datastore.DataStoreStorage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ReetApp: Application() {

    @Inject
    lateinit var dataStoreStorage: DataStoreStorage

    override fun onCreate() {
        super.onCreate()

        // init settings
        CoroutineScope(Dispatchers.IO).launch {
            val isDataStoreEmpty = dataStoreStorage.getAppTheme().first()

            if (isDataStoreEmpty == null) {
                dataStoreStorage.saveAppTheme(true)
            }
        }
    }
}