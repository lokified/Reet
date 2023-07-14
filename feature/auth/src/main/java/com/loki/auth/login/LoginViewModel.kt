package com.loki.auth.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.loki.auth.util.ext.isValidEmail
import com.loki.auth.util.ext.isValidPassword
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.User
import com.loki.remote.auth.AuthRepository
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun onAppStart(openHomeScreen: () -> Unit) {

        viewModelScope.launch {
            dataStore.getUser().collect {
                if(it.isLoggedIn) {
                    openHomeScreen()
                }
            }
        }
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
                auth.authenticate(
                    email = email,
                    password = password
                )

                //saves logged in user
                saveUser()
                isLoading.value = false
                navigateToHome()
            }
            catch (e: FirebaseException) {
                isLoading.value = false
                errorMessage.value = e.message ?: "something went wrong"
            }
        }
    }

    private fun saveUser() {

        viewModelScope.launch {
            auth.currentUser.collect { user ->
                updateUser(
                    User(
                        userId = auth.currentUserId,
                        name = user.username,
                        email = user.email,
                        isLoggedIn = true
                    )
                )
            }
        }
    }
}