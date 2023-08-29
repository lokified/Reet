package com.loki.profile.profile

data class ProfileUiState(
    val username: String = "",
    val usernameError: String = "",
    val isUsernameError: Boolean = false
)
