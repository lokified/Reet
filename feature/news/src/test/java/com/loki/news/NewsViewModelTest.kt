package com.loki.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.loki.remote.news.model.News
import com.loki.remote.news.repository.NewsRepository
import com.loki.remote.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.kotlin.mock

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: NewsViewModel
    private val repository = mockk<NewsRepository>()

    private val news = mock<List<News>>()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = NewsViewModel(repository)
    }

    @Test
    fun `getNews returns Loading`() = runTest {

        coEvery { repository.getNews() } returns flowOf(Resource.Loading())

        viewModel.getNews()

        advanceUntilIdle()

        // ensures it is loading before getting data
        assert(viewModel.newsState.value.isLoading)
    }

    @Test
    fun `getNews returns list of News`() = runTest {

        coEvery { repository.getNews() } returns flowOf(Resource.Success(news))

        viewModel.getNews()

        advanceUntilIdle()

        // ensure it has return a list of repositories
        assert(viewModel.newsState.value.newsList.isNotEmpty())
    }

    @Test
    fun `getNews returns  error message`() = runTest {
        val errorMessage = "Something Went wrong"

        coEvery { repository.getNews() } returns flowOf(Resource.Error(errorMessage))

        viewModel.getNews()

        advanceUntilIdle()

        //ensure it returns an error message
        assert(viewModel.newsState.value.errorMessage.isNotEmpty())

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}