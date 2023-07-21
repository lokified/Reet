package com.loki.auth.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.loki.auth.util.ext.isValidEmail
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalProfile
import com.loki.local.datastore.model.LocalUser
import com.loki.remote.auth.AuthRepository
import com.loki.remote.model.Profile
import com.loki.remote.profiles.ProfilesRepository
import com.loki.ui.utils.ColorUtil
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: DataStoreStorage,
    private val auth: AuthRepository,
    private val profileRepository: ProfilesRepository
): ReetViewModel(dataStore) {

    var state = mutableStateOf(LoginState())
        private set

    private val email
        get() = state.value.email

    private val password
        get() = state.value.password

    private val userName
        get() = state.value.userName

    var isProfileSheetVisible = mutableStateOf(false)
    private var localEmail = mutableStateOf("")
    private var localUsername = mutableStateOf("")
    private var localProfileBackground = mutableStateOf<Long?>(null)

    fun onEmailChange(newValue: String) {
        state.value = state.value.copy(email = newValue.trim())
        if (email.isValidEmail()) {
            state.value = state.value.copy(emailError = "", isEmailError = false)
        }
    }

    fun onPasswordChange(newValue: String) {
        state.value = state.value.copy(password = newValue.trim())
        if (password.isNotBlank()) {
            state.value = state.value.copy(passwordError = "", isPasswordError = false)
        }
    }

    fun onAppStart(openHomeScreen: () -> Unit) {
        viewModelScope.launch {
            dataStore.getUser().collect {
                if(it.isLoggedIn) {
                    openHomeScreen()
                }
            }
        }
    }

    fun onCancelProfileSheet() {
        resetField()
    }

    fun login(navigateToHome: () -> Unit) {

        if (!email.isValidEmail()) {
            state.value = state.value.copy(
                emailError = "Email is not valid",
                isEmailError = true
            )
            return
        }

        if (password.isBlank()) {
            state.value = state.value.copy(
                passwordError = "Password cannot be empty",
                isPasswordError = true
            )
            return
        }

        launchCatching {

            try {
                isLoading.value = true

                //logins user
                val user  = auth.authenticate(
                    email = email,
                    password = password
                )

                //checks if user has remote profile
                val isProfile = getRemoteProfile(user.id, user.username, user.email)


                if (isProfile) {
                    // saves logged in user
                    updateUser(
                        LocalUser(
                            userId = user.id,
                            email = user.email,
                            name = user.username,
                            isLoggedIn = true
                        )
                    )
                    //update local profile
                    updateProfile(
                        LocalProfile(
                            userName = localUsername.value,
                            profileBackground = localProfileBackground.value!!
                        )
                    )
                    isLoading.value = false
                    delay(1000L)
                    resetField()
                    navigateToHome()
                }
                isLoading.value = false

                if (!isProfile) {
                    isProfileSheetVisible.value = true
                    return@launchCatching
                }
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "something went wrong"
            }
        }
    }

    fun onUsernameChange(newValue: String) {
        state.value = state.value.copy(userName = newValue.trim())
        if (userName.isNotBlank()) {
            state.value = state.value.copy(userNameError = "", isUserNameError = false)
        }
    }

    fun setUpProfile(navigateToHome: () -> Unit) {
        if (userName.isBlank()) {
            state.value = state.value.copy(
                userNameError = "Please enter userName",
                isUserNameError = true
            )
            return
        }

        launchCatching {
            try {
                isLoading.value = true

                val color = ColorUtil.profileBackgroundColors.random()

                // setsUpProfile
                profileRepository.setUpProfile(
                    Profile(
                        userName = userName,
                        name = completeProfileUserName.value,
                        profileBackgroundColor = color,
                        userId = userId.value
                    )
                )

                delay(1000L)
                //update local profile
                updateProfile(
                    LocalProfile(
                        userName = userName,
                        profileBackground = color
                    )
                )

                delay(2000L)
                //saves logged in user
                updateUser(
                    LocalUser(
                        userId = userId.value,
                        email = localEmail.value,
                        name = completeProfileUserName.value,
                        isLoggedIn = true
                    )
                )

                resetField()
                navigateToHome()
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "something went wrong"
            }
        }
    }

    private suspend fun getRemoteProfile(id: String, name: String, email: String): Boolean {

        val remoteProfile  = profileRepository.getProfile(id)
        userId.value = id
        completeProfileUserName.value = name
        localEmail.value = email

        if (remoteProfile != null) {
            localUsername.value = remoteProfile.userName
            localProfileBackground.value = remoteProfile.profileBackgroundColor
        }

        return remoteProfile != null
    }


    private fun resetField() {
        state.value = LoginState()
    }
}