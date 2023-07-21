package com.loki.remote.profiles

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.perf.ktx.trace
import com.loki.remote.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfilesRepositoryImpl @Inject constructor(
    private val storage: FirebaseFirestore
): ProfilesRepository {

    override suspend fun getProfiles(): Flow<List<Profile>> {
        return storage.collection(USER_PROFILE_COLLECTIONS).dataObjects()
    }

    override suspend fun getProfile(userId: String): Profile? {
        val profile: Profile?

        val profiles = storage.collection(USER_PROFILE_COLLECTIONS)
            .whereEqualTo(USER_FIELD_ID, userId)
            .get().await().toObjects(Profile::class.java)

        profile = if (profiles.size == 0) null else profiles[0]

        Log.d("repo: profile", profile.toString())

        return profile
    }

    override suspend fun setUpProfile(profile: Profile) {
        trace(PROFILE_USER_TRACE) {
            storage.collection(USER_PROFILE_COLLECTIONS)
                .add(profile)
                .await()
        }
    }

    companion object {

        //collections
        const val USER_PROFILE_COLLECTIONS = "profile_collections"
        const val USER_FIELD_ID = "userId"

        //traces
        const val PROFILE_USER_TRACE = "add_profile_trace"
    }
}