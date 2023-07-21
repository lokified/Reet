package com.loki.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loki.remote.news.repository.NewsRepository
import com.loki.remote.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
): ViewModel() {

    private val _newsState = MutableStateFlow(NewsUiState())
    val newsState = _newsState.asStateFlow()

    init {
        getNews()
    }

    private fun getNews() {
        viewModelScope.launch {
            newsRepository.getNews().collect { result ->

                when(result) {
                    is Resource.Loading -> {
                        _newsState.value = NewsUiState(
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _newsState.value = NewsUiState(
                            newsList = result.data
                        )
                    }
                    is Resource.Error -> {
                        _newsState.value = NewsUiState(
                            errorMessage = result.message
                        )
                    }
                }
            }
        }
    }
}