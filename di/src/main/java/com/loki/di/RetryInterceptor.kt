package com.loki.di

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor(private val maxRetries: Int): Interceptor {

    private var retryCount = 0

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        while (!response.isSuccessful && retryCount < maxRetries) {
            retryCount++
            response.close()
            response = chain.proceed(request)
        }

        return response
    }
}