package com.loki.auth.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.loki.auth.util.ext.isValidEmail
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.LocalUser
import com.loki.remote.auth.AuthRepository
import com.loki.remote.model.Profile
import com.loki.ui.utils.ColorUtil
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: DataStoreStorage,
    private val auth: AuthRepository
): ReetViewModel(dataStore) {

    var state = mutableStateOf(LoginState())
        private set

    private val email
        get() = state.value.email

    private val password
        get() = state.value.password

    private val userName
        get() = state.value.userName

    var completeProfileUserName = mutableStateOf("")
    var isProfileComplete = mutableStateOf(false)

    var isProfileSheetVisible = mutableStateOf(false)

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
                val id  = auth.authenticate(
                    email = email,
                    password = password
                )

                //checks if user has remote profile
                getRemoteProfile(id!!)

                delay(2000L)
                isLoading.value = false

                if (isProfileComplete.value) {
                    navigateToHome()
                    //saves logged in user
                    saveLoggedInUser()
                    resetField()
                }
                else {
                    isProfileSheetVisible.value = true
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

                // setsUpProfile
                auth.setUpProfile(
                    Profile(
                        userName = userName,
                        profileBackgroundColor = ColorUtil.profileBackgroundColors.random().toString(),
                        userId = userId.value
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

    private fun getRemoteProfile(id: String) {
        viewModelScope.launch {
            val profile  = auth.getProfile(id)
            userId.value = id

            isProfileComplete.value = profile != null
            Log.d("user: profile", profile.toString())

        }
    }

    private fun saveLoggedInUser() {

        viewModelScope.launch {
            auth.currentUser.collect { user ->
                updateUser(
                    LocalUser(
                        userId = user.id,
                        name = user.username,
                        email = user.email,
                        isLoggedIn = true
                    )
                )
            }
        }
    }

    private fun resetField() {
        state.value = LoginState()
    }
}