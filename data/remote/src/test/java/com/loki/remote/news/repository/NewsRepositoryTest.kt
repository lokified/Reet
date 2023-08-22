package com.loki.remote.news.repository

import com.loki.remote.news.model.News
import com.loki.remote.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.kotlin.mock

class NewsRepositoryTest {

    companion object {
        fun mockGetNewsList(
            flowReturn: Flow<Resource<List<News>>>
        ) = object : NewsRepository {
            override suspend fun getNews(): Flow<Resource<List<News>>> = flowReturn
        }
    }

    @Test
    fun `getNews starts with loading returns Resource Loading` () = runBlocking {
        val news = mock<List<News>>()

        val newsRepository = mockGetNewsList(
            flow {
                emit(Resource.Loading())
                emit(Resource.Success(news))
            }
        )

        val result = newsRepository.getNews().first()

        assert(result is Resource.Loading)
    }

    @Test
    fun `getNews with data returns Resource Success` () = runBlocking {
        val news = mock<List<News>>()

        val newsRepository = mockGetNewsList(
            flow {
                emit(Resource.Loading())
                emit(Resource.Success(news))
            }
        )

        val result = newsRepository.getNews().last()

        assert(result is Resource.Success && result.data != emptyList<List<News>>())
    }

    @Test
    fun `getNews with error returns Resource Error` () = runBlocking {

        val newsRepository = mockGetNewsList(
            flow {
                emit(Resource.Error(message = "An unexpected error occurred"))
            }
        )

        val result = newsRepository.getNews().last()

        assert(result is Resource.Error && result.message == "An unexpected error occurred")
    }
}