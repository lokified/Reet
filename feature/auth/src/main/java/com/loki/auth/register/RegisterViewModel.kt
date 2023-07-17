package com.loki.auth.register

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.FirebaseException
import com.loki.auth.util.ext.isValidEmail
import com.loki.auth.util.ext.passwordMatches
import com.loki.local.datastore.DataStoreStorage
import com.loki.remote.auth.AuthRepository
import com.loki.remote.model.Profile
import com.loki.remote.profiles.ProfilesRepository
import com.loki.ui.utils.ColorUtil
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val dataStore: DataStoreStorage,
    private val auth: AuthRepository,
    private val profile: ProfilesRepository
): ReetViewModel(dataStore) {

    var state = mutableStateOf(RegisterState())
        private set

    private val firstName
        get() = state.value.firstName
    private val lastName
        get() = state.value.lastName
    private val email
        get() = state.value.email
    private val password
        get() = state.value.password
    private val conPassword
        get() = state.value.conPassword
    private val userName
        get() = state.value.userName

    fun onFirstNameChange(newValue: String) {
        state.value = state.value.copy(firstName = newValue.trim())
        if (firstName.isNotBlank()) {
            state.value = state.value.copy(firstNameError = "", isFirstNameError = false)
        }
    }

    fun onLastNameChange(newValue: String) {
        state.value = state.value.copy(lastName = newValue.trim())
        if (lastName.isNotBlank()) {
            state.value = state.value.copy(lastNameError = "", isLastNameError = false)
        }
    }

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

    fun onConPasswordChange(newValue: String) {
        state.value = state.value.copy(conPassword = newValue.trim())
        if (conPassword.passwordMatches(password)) {
            state.value = state.value.copy(conPasswordError = "", isConPasswordError = false)
        }
    }

    fun onUsernameChange(newValue: String) {
        state.value = state.value.copy(userName = newValue.trim())
        if (userName.isNotBlank()) {
            state.value = state.value.copy(userNameError = "", isUserNameError = false)
        }
    }

    fun onCancelProfileSheet() {
        resetField()
    }

    fun setUpProfile(navigateToLogin: () -> Unit) {
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

                // sets up remote Profile
                profile.setUpProfile(
                    Profile(
                        userName = userName,
                        name = "$firstName $lastName",
                        profileBackgroundColor = ColorUtil.profileBackgroundColors.random(),
                        userId = userId.value
                    )
                )

                resetField()
                isLoading.value = false
                navigateToLogin()
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "something went wrong"
            }
        }
    }

    fun register(onRegister: () -> Unit) {

        if (firstName.isBlank()) {
            state.value = state.value.copy(
                firstNameError = "Name cannot be empty",
                isFirstNameError = true
            )
            return
        }

        if (lastName.isBlank()) {
            state.value = state.value.copy(
                lastNameError = "Name cannot be empty",
                isLastNameError = true
            )
            return
        }

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

        if (!conPassword.passwordMatches(password)) {
            state.value = state.value.copy(
                conPasswordError = "Password does not match",
                isConPasswordError = true
            )
            return
        }

        launchCatching {

            try {
                isLoading.value = true

                //creates new account
                val id = auth.createAccount(
                    names = "$firstName $lastName",
                    email = email,
                    password = password
                )

                completeProfileUserName.value = "$firstName $lastName"

                userId.value = id!!

                isLoading.value = false
                onRegister()
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "something went wrong"
            }
        }
    }

    private fun resetField() {
        state.value = RegisterState()
    }
}