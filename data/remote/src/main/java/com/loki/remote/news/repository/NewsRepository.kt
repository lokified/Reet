package com.loki.remote.news.repository

import com.loki.remote.util.Resource
import com.loki.remote.news.model.News
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    suspend fun getNews(): Flow<Resource<List<News>>>
}