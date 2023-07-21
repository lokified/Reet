package com.loki.remote.news.repository

import com.loki.remote.news.api.RipotiApi
import com.loki.remote.news.model.News
import com.loki.remote.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: RipotiApi
): NewsRepository {

    override suspend fun getNews(): Flow<Resource<List<News>>> = flow {

        try {
            emit(Resource.Loading())
            emit(Resource.Success(api.getNews()))
        }
        catch (e: HttpException) {
            emit(Resource.Error(e.message ?: "An unexpected error occurred"))
        }
        catch (e: IOException) {
            emit(Resource.Error("check your internet connection"))
        }
    }
}