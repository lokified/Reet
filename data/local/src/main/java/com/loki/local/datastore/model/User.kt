package com.loki.local.datastore.model

data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val isLoggedIn: Boolean = false
)
