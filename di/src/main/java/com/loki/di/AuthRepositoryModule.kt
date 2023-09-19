package com.loki.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.loki.remote.auth.AuthRepository
import com.loki.remote.auth.AuthRepositoryImpl
import com.loki.remote.profiles.ProfilesRepository
import com.loki.remote.profiles.ProfilesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImpl(auth)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(storage: FirebaseFirestore, store: FirebaseStorage): ProfilesRepository {
        return ProfilesRepositoryImpl(storage, store)
    }
}