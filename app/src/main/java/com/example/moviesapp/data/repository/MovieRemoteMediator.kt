package com.example.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.db.MovieDatabase
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieRemoteKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalPagingApi
class MovieRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val database: MovieDatabase
) : RemoteMediator<Int, Movie>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevPage
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKeys?.nextPage
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextPage
                }
            }

            val apiResponse = apiService.getMovies(page)
            val results = apiResponse.results
            val endOfPaginationReached = results.isEmpty()
            val prevPage = if (page > 1) page - 1 else null
            val nextPage = if (endOfPaginationReached) null else page + 1
            val remoteKeys = results.map {
                MovieRemoteKeys(
                    id = it.id,
                    prevPage = prevPage,
                    nextPage = nextPage
                )
            }
            CoroutineScope(Dispatchers.IO).launch {
                database.movieRemoteKeysDao().deleteAllRemoteKeys()
                database.movieRemoteKeysDao().addAllRemoteKeys(remoteKeys = remoteKeys)
                database.movieDao().insertAll(movie = results)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Movie>
    ): MovieRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.movieRemoteKeysDao().getRemoteKeys(id = id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, Movie>
    ): MovieRemoteKeys? {
        return state.firstItemOrNull()?.let { movie ->
            database.movieRemoteKeysDao().getRemoteKeys(id = movie.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, Movie>
    ): MovieRemoteKeys? {
        return state.lastItemOrNull()?.let { movie ->
            database.movieRemoteKeysDao().getRemoteKeys(id = movie.id)
        }
    }
}