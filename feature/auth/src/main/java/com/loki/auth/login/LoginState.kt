package com.loki.auth.login

data class LoginState(
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val password: String = "",
    val isPasswordError: Boolean = false,
    val passwordError: String = "",
    val userName: String = "",
    val isUserNameError: Boolean = false,
    val userNameError: String = "",
)

data class LocalProfileState(
    val id: String = "",
    val username: String = "",
    val profileBackground: Long? = null
)
