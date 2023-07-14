package com.loki.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.loki.local.datastore.model.User
import kotlinx.coroutines.flow.Flow

interface DataStoreStorage {

    suspend fun saveUser(user: User)

    suspend fun getUser(): Flow<User>

    object UserPreferences {
        val USER_ID_KEY = stringPreferencesKey("user_id_key")
        val USER_NAME_KEY = stringPreferencesKey("user_name_key")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email_key")
        val USER_LOGGEDIN_KEY = booleanPreferencesKey("user_loggedin_key")
    }
}