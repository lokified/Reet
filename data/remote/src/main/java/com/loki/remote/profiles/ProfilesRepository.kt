package com.loki.remote.profiles

import com.loki.remote.model.Profile
import kotlinx.coroutines.flow.Flow

interface ProfilesRepository {

    suspend fun getProfiles(): Flow<List<Profile>>

    suspend fun getProfile(userId: String): Profile?

    suspend fun setUpProfile(profile: Profile)

    suspend fun updateUsername(profile: Profile)
}