package com.loki.di

import com.loki.remote.news.api.RipotiApi
import com.loki.remote.news.repository.NewsRepository
import com.loki.remote.news.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val MAX_RETRIES = 3
private const val BASE_URL = "https://ripoti-news-api.onrender.com/"

@Module
@InstallIn(SingletonComponent::class)
object NewsRepositoryModule {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(RetryInterceptor(MAX_RETRIES))
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(): RipotiApi {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(RipotiApi::class.java)
    }


    @Singleton
    @Provides
    fun provideNewsRepository(api: RipotiApi): NewsRepository {
        return NewsRepositoryImpl(api)
    }
}