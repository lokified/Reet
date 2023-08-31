package com.loki.local.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.loki.local.datastore.model.LocalProfile
import com.loki.local.datastore.model.LocalUser
import kotlinx.coroutines.flow.Flow

interface DataStoreStorage {

    suspend fun saveUser(localUser: LocalUser)

    suspend fun getUser(): Flow<LocalUser>

    suspend fun saveProfile(localProfile: LocalProfile)

    suspend fun getProfile(): Flow<LocalProfile>

    suspend fun saveAppTheme(isDarkTheme: Boolean)

    suspend fun getAppTheme(): Flow<Boolean>

    object UserPreferences {
        val USER_ID_KEY = stringPreferencesKey("user_id_key")
        val USER_NAME_KEY = stringPreferencesKey("user_name_key")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email_key")
        val USER_LOGGEDIN_KEY = booleanPreferencesKey("user_loggedin_key")
    }

    object ProfilePreference {
        val PROFILE_ID_KEY = stringPreferencesKey("profile_id_key")
        val USER_USERNAME_KEY = stringPreferencesKey("user_username_key")
        val USER_USERNAME_INITIALS_KEY = stringPreferencesKey("user_username_initials_key")
        val USER_IMAGE_KEY = stringPreferencesKey("user_image_key")
        val USER_BACKGROUND_KEY = longPreferencesKey("user_background_key")
    }

    object ThemePreference {
        val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme_key")
    }
}