package com.loki.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.loki.remote.auth.AuthRepository
import com.loki.remote.auth.AuthRepositoryImpl
import com.loki.remote.comments.CommentsRepository
import com.loki.remote.comments.CommentsRepositoryImpl
import com.loki.remote.profiles.ProfilesRepository
import com.loki.remote.profiles.ProfilesRepositoryImpl
import com.loki.remote.reports.ReportsRepository
import com.loki.remote.reports.ReportsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideAuthentication(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage

}