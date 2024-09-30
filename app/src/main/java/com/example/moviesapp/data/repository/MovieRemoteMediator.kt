package com.example.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.db.MovieDatabase
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieRemoteKeys
import javax.inject.Inject

@ExperimentalPagingApi
class MovieRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val database: MovieDatabase
) : RemoteMediator<Int, Movie>() {
    private var isInitialLoad = true
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = database.movieRemoteKeysDao().getRemoteKeys()
                    val nextPage = remoteKeys?.nextPage?.minus(1) ?: 1
                    nextPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = database.movieRemoteKeysDao().getRemoteKeys()
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextPage
                }
            }

            val apiResponse = apiService.getMovies(page)
            val results = apiResponse.results
            val endOfPaginationReached = results.isEmpty()

            val nextPage = if (endOfPaginationReached) null else page + 1

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    if (isInitialLoad) {
                        database.movieRemoteKeysDao().deleteAllRemoteKeys()
                        database.movieDao().clearAll()
                        isInitialLoad = false
                    }
                }
                database.movieDao().insertAll(movie = results)
                database.movieRemoteKeysDao().addAllRemoteKeys(
                    MovieRemoteKeys(nextPage = nextPage)
                )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}