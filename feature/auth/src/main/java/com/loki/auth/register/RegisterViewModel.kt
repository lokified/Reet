package com.loki.auth.register

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.FirebaseException
import com.loki.auth.util.ext.isValidEmail
import com.loki.auth.util.ext.passwordMatches
import com.loki.local.datastore.DataStoreStorage
import com.loki.remote.auth.AuthRepository
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    dataStore: DataStoreStorage,
    private val auth: AuthRepository
): ReetViewModel(dataStore) {

    var state = mutableStateOf(RegisterState())
        private set

    private val name
        get() = state.value.name
    private val email
        get() = state.value.email
    private val password
        get() = state.value.password
    private val conPassword
        get() = state.value.conPassword

    fun onNameChange(newValue: String) {
        state.value = state.value.copy(name = newValue)
        if (name.isNotBlank()) {
            state.value = state.value.copy(nameError = "", isNameError = false)
        }
    }

    fun onEmailChange(newValue: String) {
        state.value = state.value.copy(email = newValue)
        if (email.isValidEmail()) {
            state.value = state.value.copy(emailError = "", isEmailError = false)
        }
    }

    fun onPasswordChange(newValue: String) {
        state.value = state.value.copy(password = newValue)
        if (password.isNotBlank()) {
            state.value = state.value.copy(passwordError = "", isPasswordError = false)
        }
    }

    fun onConPasswordChange(newValue: String) {
        state.value = state.value.copy(conPassword = newValue)
        if (conPassword.passwordMatches(password)) {
            state.value = state.value.copy(conPasswordError = "", isConPasswordError = false)
        }
    }

    fun register(navigateToLogin: () -> Unit) {

        if (name.isBlank()) {
            state.value = state.value.copy(
                nameError = "Name cannot be empty",
                isNameError = true
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
                auth.createAccount(
                    names = name,
                    email = email,
                    password = password
                )

                isLoading.value = false
                navigateToLogin()
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "something went wrong"
            }
        }
    }
}