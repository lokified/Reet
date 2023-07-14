package com.loki.remote.auth

import com.loki.remote.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun createAccount(names: String, email: String, password: String)
    suspend fun updateUser(name: String, profilePhoto: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}