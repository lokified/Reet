package com.loki.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalProfile
import com.loki.local.datastore.model.LocalUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


open class ReetViewModel(
    private val dataStore: DataStoreStorage
): ViewModel() {

    // app theme
    val isDarkTheme = mutableStateOf(true)

    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch {
            try {
                isLoading.value = true
                block()
                isLoading.value = false
            } catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "Something went wrong"
            }
        }

    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    //user values
    private val _localUser = MutableStateFlow(LocalUser())
    val localUser = _localUser.asStateFlow()

    //profile values
    private val _localProfile = MutableStateFlow(LocalProfile())
    val localProfile = _localProfile.asStateFlow()

    init {
        getUser()
        getLocalProfile()
        getAppTheme()
    }

    fun getUser() {
        viewModelScope.launch {
            dataStore.getUser().collect {
                _localUser.value = LocalUser(
                    userId = it.userId,
                    name = it.name,
                    email = it.email,
                    isLoggedIn = it.isLoggedIn,
                )
            }
        }
    }

    fun updateUser(localUser: LocalUser) {
        viewModelScope.launch {
            dataStore.saveUser(localUser)
        }
    }

    fun getLocalProfile() {
        viewModelScope.launch {
            dataStore.getProfile().collect {
                _localProfile.value =  LocalProfile(
                    id = it.id,
                    userName = it.userName,
                    userNameInitials = it.userNameInitials,
                    profileBackground = it.profileBackground,
                    profileImage = it.profileImage
                )
            }
        }
    }

    fun updateProfile(localProfile: LocalProfile) {
        viewModelScope.launch {
            dataStore.saveProfile(localProfile)
        }
    }

    private fun getAppTheme() {
        viewModelScope.launch {
            dataStore.getAppTheme().collect {
                isDarkTheme.value = it
            }
        }
    }
}