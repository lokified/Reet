package com.loki.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_EMAIL_KEY
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_ID_KEY
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_LOGGEDIN_KEY
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_NAME_KEY
import com.loki.local.datastore.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreStorageImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
): DataStoreStorage {

    override suspend fun saveUser(user: User) {

        datastore.edit { preference ->
            preference[USER_ID_KEY] = user.userId
            preference[USER_NAME_KEY] = user.name
            preference[USER_EMAIL_KEY] = user.email
            preference[USER_LOGGEDIN_KEY] = user.isLoggedIn
        }
    }

    override suspend fun getUser(): Flow<User> {

        return datastore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY] ?: ""
            val name = preferences[USER_NAME_KEY] ?: ""
            val email = preferences[USER_EMAIL_KEY] ?: ""
            val isLoggedIn = preferences[USER_LOGGEDIN_KEY] ?: false

            User(userId, name, email, isLoggedIn)
        }
    }
}