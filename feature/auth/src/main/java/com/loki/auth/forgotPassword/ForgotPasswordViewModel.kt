package com.loki.auth.forgotPassword

import androidx.compose.runtime.mutableStateOf
import com.loki.auth.util.ext.isValidEmail
import com.loki.local.datastore.DataStoreStorage
import com.loki.remote.auth.AuthRepository
import com.loki.ui.viewmodel.ReetViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    dataStore: DataStoreStorage,
    private val auth: AuthRepository
): ReetViewModel(dataStore) {

    var state = mutableStateOf(ForgotPasswordState())
        private set

    private val email
        get() = state.value.email

    fun onEmailChange(newValue: String) {
        state.value = state.value.copy(email = newValue.trim())
        if (email.isValidEmail()) {
            state.value = state.value.copy(emailError = "", isEmailError = false)
        }
    }

    fun sendResetLink(onSuccess: () -> Unit) {
        if (!email.isValidEmail()) {
            state.value = state.value.copy(
                emailError = "Email is not valid",
                isEmailError = true
            )
            return
        }

        launchCatching {
            auth.sendRecoveryEmail(email)
            onSuccess()
        }
    }
}