package com.loki.profile

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalProfile
import com.loki.local.datastore.model.LocalUser
import com.loki.profile.profile.ProfileUiState
import com.loki.remote.auth.AuthRepository
import com.loki.remote.model.Profile
import com.loki.remote.profiles.ProfilesRepository
import com.loki.ui.utils.ext.toInitials
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private var editableProfile = mutableStateOf(Profile())

    init {
        state.value = state.value.copy(username = localProfile.value.userName)
        editableProfile.value = Profile(
            id = localProfile.value.id,
            name = localUser.value.name,
            userName = localProfile.value.userName,
            profileBackgroundColor = localProfile.value.profileBackground,
            userId = localUser.value.userId,
            profileImage = localProfile.value.profileImage
        )
    }

    fun onUsernameChange(newValue: String) {
        state.value = state.value.copy(username = newValue)
        if (username.isNotBlank()) {
            state.value = state.value.copy(usernameError = "", isUsernameError = false)
        }
    }

    fun updateProfilePicture(imageUri: Uri, onSuccess: () -> Unit) {
        launchCatching {
            profilesRepository.updateProfileImage(
                editableProfile.value.copy(
                    profileImage = imageUri.toString()
                )
            )

            updateProfile(
                localProfile.value.copy(
                    profileImage = imageUri.toString()
                )
            )
            onSuccess()
            message.value = "Profile Updated"
        }
    }

    fun updateUsername(onSuccess: () -> Unit) {
        launchCatching {
            profilesRepository.updateUsername(
                editableProfile.value.copy(
                    userName = username
                )
            )
            updateProfile(
                localProfile.value.copy(
                    userName = username
                )
            )
            onSuccess()
            message.value = "Username Updated"
        }
    }

    fun logOut(navigateToLogin: () -> Unit) {
        launchCatching {
            auth.signOut()
            updateUser(LocalUser())
            updateProfile(LocalProfile())
            navigateToLogin()
        }
    }
}