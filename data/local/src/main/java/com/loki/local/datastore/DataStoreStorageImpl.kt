package com.loki.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.loki.local.datastore.DataStoreStorage.ProfilePreference.PROFILE_ID_KEY
import com.loki.local.datastore.DataStoreStorage.ProfilePreference.USER_BACKGROUND_KEY
import com.loki.local.datastore.DataStoreStorage.ProfilePreference.USER_USERNAME_KEY
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_EMAIL_KEY
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_ID_KEY
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_LOGGEDIN_KEY
import com.loki.local.datastore.DataStoreStorage.UserPreferences.USER_NAME_KEY
import com.loki.local.datastore.model.LocalProfile
import com.loki.local.datastore.model.LocalUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreStorageImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
): DataStoreStorage {

    override suspend fun saveUser(localUser: LocalUser) {

        datastore.edit { preference ->
            preference[USER_ID_KEY] = localUser.userId
            preference[USER_NAME_KEY] = localUser.name
            preference[USER_EMAIL_KEY] = localUser.email
            preference[USER_LOGGEDIN_KEY] = localUser.isLoggedIn
        }
    }

    override suspend fun getUser(): Flow<LocalUser> {

        return datastore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY] ?: ""
            val name = preferences[USER_NAME_KEY] ?: ""
            val email = preferences[USER_EMAIL_KEY] ?: ""
            val isLoggedIn = preferences[USER_LOGGEDIN_KEY] ?: false

            LocalUser(userId, name, email, isLoggedIn)
        }
    }

    override suspend fun saveProfile(localProfile: LocalProfile) {
        datastore.edit { preference ->
            preference[PROFILE_ID_KEY] = localProfile.id
            preference[USER_USERNAME_KEY] = localProfile.userName
            preference[USER_BACKGROUND_KEY] = localProfile.profileBackground!!
        }
    }

    override suspend fun getProfile(): Flow<LocalProfile> {

        return datastore.data.map { preferences ->
            val id = preferences[PROFILE_ID_KEY] ?: ""
            val username = preferences[USER_USERNAME_KEY] ?: ""
            val background = preferences[USER_BACKGROUND_KEY] ?: 0xFFF1736A

            LocalProfile(id, username, background)
        }
    }
}