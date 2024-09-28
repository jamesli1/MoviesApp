package com.example.moviesapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    override suspend fun getMovies(): Pager<Int, Movie> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviesPagingSource(apiService)
        }
    }

    override suspend fun getMovieDetails(id: Int): MovieDetails {
        return withContext(ioDispatcher) {
            apiService.getMovieDetails(id)
        }
    }
}