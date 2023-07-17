package com.loki.local.datastore.model

data class LocalUser(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val isLoggedIn: Boolean = false
)
