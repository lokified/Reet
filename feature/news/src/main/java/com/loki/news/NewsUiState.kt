package com.loki.news

import com.loki.remote.news.model.News

data class NewsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val newsList: List<News> = emptyList()
)
