package com.loki.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.local.datastore.DataStoreStorage
import com.loki.local.datastore.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class ReetViewModel(
    private val dataStore: DataStoreStorage
    ): ViewModel() {

    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            block = block
        )

    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    val user = mutableStateOf(com.loki.remote.model.User())



    fun updateUser(user: User) {

        viewModelScope.launch {
            dataStore.saveUser(
                User(
                    userId = user.userId,
                    name = user.name,
                    email = user.email,
                    isLoggedIn = user.isLoggedIn
                )
            )
        }
    }
}