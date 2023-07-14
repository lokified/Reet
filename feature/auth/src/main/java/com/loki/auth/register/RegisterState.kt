package com.loki.auth.register

data class RegisterState(
    val name: String = "",
    val isNameError: Boolean = false,
    val nameError: String = "",
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val password: String = "",
    val isPasswordError: Boolean = false,
    val passwordError: String = "",
    val conPassword: String = "",
    val isConPasswordError: Boolean = false,
    val conPasswordError: String = "",
)
