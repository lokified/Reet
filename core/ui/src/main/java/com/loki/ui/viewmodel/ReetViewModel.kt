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

    //user setup values
    val localUser = mutableStateOf(LocalUser())
    var userId = mutableStateOf("")
    var completeProfileUserName = mutableStateOf("")

    //profile values
    val userInitial = mutableStateOf("")
    var localProfile = mutableStateOf(LocalProfile())

    init {
        getAppTheme()
    }

    fun getUser() {
        viewModelScope.launch {
            dataStore.getUser().collect {
                localUser.value = LocalUser(
                    userId = it.userId,
                    name = it.name,
                    email = it.email,
                    isLoggedIn = it.isLoggedIn,
                )

                var firstName = "R"
                var lastName = "U"

                if (it.name.isNotBlank()) {
                    val userList = it.name.split(" ")
                    firstName = userList[0][0].toString()
                    lastName = userList[1][0].toString()
                }

                userInitial.value = firstName + lastName
            }
        }
    }

    suspend fun updateUser(localUser: LocalUser) {
        viewModelScope.launch {
            dataStore.saveUser(localUser)
        }
    }

    fun getLocalProfile() {
        viewModelScope.launch {
            dataStore.getProfile().collect {
                localProfile.value = LocalProfile(
                    userName = it.userName,
                    profileBackground = it.profileBackground
                )
                Log.d("profile:reet", it.userName)

            }
        }
    }

    suspend fun updateProfile(localProfile: LocalProfile) {
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