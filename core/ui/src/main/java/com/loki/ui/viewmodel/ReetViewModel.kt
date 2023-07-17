package com.loki.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalProfile
import com.loki.local.datastore.model.LocalUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class ReetViewModel(
    private val dataStore: DataStoreStorage
    ): ViewModel() {

    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            block = block
        )

    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    //user setup values
    val localUser = mutableStateOf(LocalUser())
    var userId = mutableStateOf("")
    var completeProfileUserName = mutableStateOf("")

    //profile values
    val userInitial = mutableStateOf("")
    val profileBackground = mutableStateOf(0xFFF1736A)
    val profileUsername = mutableStateOf("")

    fun getUser() {
        launchCatching {
            dataStore.getUser().collect {
                localUser.value = localUser.value.copy(
                    userId = it.userId,
                    name = it.name,
                    email = it.email,
                    isLoggedIn = it.isLoggedIn,
                )

                var firstName = ""
                var lastName = ""

                if (it.name.isNotBlank()) {
                    val userList = it.name.split(" ")
                    firstName = userList[0][0].toString()
                    lastName = userList[1][0].toString()
                }

                userInitial.value = firstName + lastName
            }
        }
    }

    fun updateUser(localUser: LocalUser) {

        viewModelScope.launch {
            dataStore.saveUser(
                LocalUser(
                    userId = localUser.userId,
                    name = localUser.name,
                    email = localUser.email,
                    isLoggedIn = localUser.isLoggedIn
                )
            )
        }
    }

    fun getLocalProfile() {
        viewModelScope.launch {
            dataStore.getProfile().collect {
                profileUsername.value = it.userName
                profileBackground.value = it.profileBackground ?: 0xFFF1736A
            }
        }
    }

    fun updateProfile(localProfile: LocalProfile) {

        viewModelScope.launch {
            dataStore.saveProfile(
                LocalProfile(
                    userName = localProfile.userName,
                    profileBackground = localProfile.profileBackground
                )
            )
        }
    }
}