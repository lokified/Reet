package com.loki.profile

import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalUser
import com.loki.remote.auth.AuthRepository
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    dataStore: DataStoreStorage,
    private val auth: AuthRepository
): ReetViewModel(dataStore){


    fun logOut(navigateToLogin: () -> Unit) {
        launchCatching {
            auth.signOut()
            updateUser(LocalUser())
            navigateToLogin()
        }
    }
}