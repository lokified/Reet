package com.loki.remote.news.api

import com.loki.remote.news.model.News
import retrofit2.http.GET

interface RipotiApi {

    @GET("accident-news")
    suspend fun getNews(): List<News>

}