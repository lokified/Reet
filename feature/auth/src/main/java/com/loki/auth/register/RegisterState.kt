package com.loki.auth.register

data class RegisterState(
    val firstName: String = "",
    val isFirstNameError: Boolean = false,
    val firstNameError: String = "",
    val lastName: String = "",
    val isLastNameError: Boolean = false,
    val lastNameError: String = "",
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val password: String = "",
    val isPasswordError: Boolean = false,
    val passwordError: String = "",
    val conPassword: String = "",
    val isConPasswordError: Boolean = false,
    val conPasswordError: String = "",
    val userName: String = "",
    val isUserNameError: Boolean = false,
    val userNameError: String = "",
)
