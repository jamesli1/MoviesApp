package com.example.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.db.MovieDao
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @OptIn(ExperimentalPagingApi::class)
@Inject constructor(
    private val apiService: ApiService,
    private val movieDao: MovieDao,
    private val movieRemoteMediator: MovieRemoteMediator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovies(): Pager<Int, Movie> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = movieRemoteMediator){
            movieDao.getAll()
        }
    }

    override suspend fun getMovieDetails(id: Int): MovieDetails {
        return withContext(ioDispatcher) {
            val cachedDetails = movieDao.getDetailsById(id)
            if (cachedDetails != null) {
                return@withContext cachedDetails
            }

            val response = apiService.getMovieDetails(id)
            movieDao.insertDetails(response)
            return@withContext response
        }
    }
}