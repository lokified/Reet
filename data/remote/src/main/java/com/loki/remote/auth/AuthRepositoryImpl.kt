package com.loki.remote.auth

import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.perf.ktx.trace
import com.loki.remote.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): AuthRepository {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener = FirebaseAuth.AuthStateListener { auth ->
                this.trySend(auth.currentUser?.let {

                    User(
                        id = it.uid,
                        username = it.displayName!!,
                        email = it.email!!
                    )
                } ?: User())
            }

            auth.addAuthStateListener(listener)
            awaitClose {
                auth.removeAuthStateListener(listener)
            }
        }

    override suspend fun authenticate(email: String, password: String): User {
        trace(LOGIN_USER_TRACE) {
            val user = auth.signInWithEmailAndPassword(email, password).await()?.user
            return User(id = user!!.uid, username = user.displayName!!, email = user.email!!)
        }
    }

    override suspend fun createAccount(names: String, email: String, password: String): String? {
        trace(SIGNUP_USER_TRACE) {
            return auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val request = UserProfileChangeRequest.Builder().apply {
                        displayName = names
                    }.build()

                    auth.currentUser?.updateProfile(request)
                }

            }.await().user?.uid
        }
    }

    override suspend fun updateUser(name: String?, profilePhoto: String?) {
        trace(UPDATE_USER_TRACE) {
            val request = UserProfileChangeRequest.Builder()
            if (name != null) {
                request.apply {
                    displayName = name
                }
            }

            if (profilePhoto != null) {
                request.apply {
                    photoUri = profilePhoto.toUri()
                }
            }
            auth.currentUser?.updateProfile(request.build())
        }
    }

    override suspend fun sendRecoveryEmail(email: String) {
        trace(PASSWORD_CHANGE_TRACE) {
            auth.sendPasswordResetEmail(email).await()
        }
    }

    override suspend fun deleteAccount() {
        trace(DELETE_ACCOUNT_TRACE) {
            auth.currentUser!!.delete().await()
        }
    }

    override suspend fun signOut() {
        trace(LOGOUT_TRACE) {
            auth.signOut()
        }
    }

    companion object {

        //traces
        const val LOGIN_USER_TRACE = "login_trace"
        const val SIGNUP_USER_TRACE = "signup_trace"
        const val UPDATE_USER_TRACE = "update_trace"
        const val PASSWORD_CHANGE_TRACE = "password_change_trace"
        const val DELETE_ACCOUNT_TRACE = "delete_account_trace"
        const val LOGOUT_TRACE = "logout_trace"
    }
}