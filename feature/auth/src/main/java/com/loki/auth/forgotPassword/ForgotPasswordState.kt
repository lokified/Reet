package com.loki.auth.forgotPassword

data class ForgotPasswordState(
    val email: String = "",
    val emailError: String = "",
    val isEmailError: Boolean = false
)
