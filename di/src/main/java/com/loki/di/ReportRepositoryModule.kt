package com.loki.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.loki.remote.auth.AuthRepository
import com.loki.remote.comments.CommentsRepository
import com.loki.remote.comments.CommentsRepositoryImpl
import com.loki.remote.profiles.ProfilesRepository
import com.loki.remote.reports.ReportsRepository
import com.loki.remote.reports.ReportsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReportRepositoryModule {

    @Provides
    @Singleton
    fun provideReportsRepository(
        store: FirebaseStorage,
        storage: FirebaseFirestore,
        auth: AuthRepository,
        profile: ProfilesRepository
    ) : ReportsRepository {
        return ReportsRepositoryImpl(store, storage, auth, profile)
    }

    @Provides
    @Singleton
    fun provideCommentsRepository(storage: FirebaseFirestore, report: ReportsRepository) : CommentsRepository {
        return CommentsRepositoryImpl(storage, report)
    }
}