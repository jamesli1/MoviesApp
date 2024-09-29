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

            val response = apiService.getMovies(page)
            val endOfPaginationReached = response.results.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.movieRemoteKeysDao().deleteAllRemoteKeys()
                }

                val prevPage = if (page == 1) null else page - 1
                val nextPage = if (endOfPaginationReached) null else page + 1

                val keys = response.results.map { movie ->
                    MovieRemoteKeys(
                        id = movie.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )
                }

                database.movieRemoteKeysDao().addAllRemoteKeys(remoteKeys = keys)
                database.movieDao().insertAll(movie = response.results)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
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