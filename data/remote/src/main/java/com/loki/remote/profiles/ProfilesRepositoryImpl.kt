package com.loki.remote.profiles

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.perf.ktx.trace
import com.google.firebase.storage.FirebaseStorage
import com.loki.remote.model.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfilesRepositoryImpl @Inject constructor(
    private val storage: FirebaseFirestore,
    private val store: FirebaseStorage
): ProfilesRepository {

    override suspend fun getProfiles(): Flow<List<Profile>> {
        return storage.collection(USER_PROFILE_COLLECTIONS).dataObjects()
    }

    override suspend fun getProfile(userId: String): Profile? {
        val profile: Profile?

        val profiles = storage.collection(USER_PROFILE_COLLECTIONS)
            .whereEqualTo(USER_FIELD_ID, userId)
            .get().await().toObjects(Profile::class.java)

        profile = if (profiles.size == 0) {
            null
        } else {
            profiles[0]
        }

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

    override suspend fun updateUsername(profile: Profile) {
        trace(UPDATE_PROFILE_USERNAME_TRACE) {
            storage.collection(USER_PROFILE_COLLECTIONS)
                .document(profile.id)
                .set(profile)
                .await()
        }
    }

    override suspend fun updateProfileImage(profile: Profile) {
        trace(UPDATE_PROFILE_IMAGE_TRACE) {

            val imageName = "${profile.userName}-${profile.userId}"
            val url = addProfileImageToFirebaseStorage(
                Uri.parse(profile.profileImage),
                imageName
            )

            val profileWithImageUrl = profile.copy(
                profileImage = url.toString()
            )

            storage.collection(USER_PROFILE_COLLECTIONS)
                .document(profile.id)
                .set(profileWithImageUrl)
                .await()
        }
    }

    private suspend fun addProfileImageToFirebaseStorage(imageUri: Uri, imageName: String): Uri{

        return store.reference.child(PROFILE_IMAGE_REFERENCE)
            .child(imageName)
            .putFile(imageUri).await()
            .storage.downloadUrl.await()
    }

    companion object {

        //collections
        const val USER_PROFILE_COLLECTIONS = "profile_collections"
        const val USER_FIELD_ID = "userId"
        const val PROFILE_IMAGE_REFERENCE = "profile_images"

        //traces
        const val PROFILE_USER_TRACE = "add_profile_trace"
        const val UPDATE_PROFILE_USERNAME_TRACE = "update_profile_username_trace"
        const val UPDATE_PROFILE_IMAGE_TRACE = "update_profile_image_trace"
    }
}