package com.loki.profile

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.FirebaseException
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalProfile
import com.loki.local.datastore.model.LocalUser
import com.loki.remote.auth.AuthRepository
import com.loki.remote.model.Profile
import com.loki.remote.profiles.ProfilesRepository
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    dataStore: DataStoreStorage,
    private val auth: AuthRepository,
    private val profilesRepository: ProfilesRepository
): ReetViewModel(dataStore){

    var state = mutableStateOf(ProfileUiState())
        private set

    private val username
        get() = state.value.username

    private var profileId = mutableStateOf("")

    init {
        getUser()
        getLocalProfile()
        state.value = state.value.copy(username = localProfile.value.userName)
    }

    fun onUsernameChange(newValue: String) {
        state.value = state.value.copy(username = newValue.trim())
        if (username.isNotBlank()) {
            state.value = state.value.copy(usernameError = "", isUsernameError = false)
        }
    }

    fun updateUsername() {
        launchCatching {
            try {
                isLoading.value = true

                profilesRepository.updateUsername(
                    Profile(
                        id = profileId.value,
                        userName = username
                    )
                )

                isLoading.value = false
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "Something went wrong"
            }
        }
    }

    fun logOut(navigateToLogin: () -> Unit) {
        launchCatching {
            auth.signOut()
            updateUser(LocalUser(name = "R U"))
            updateProfile(LocalProfile())
            navigateToLogin()
        }
    }
}