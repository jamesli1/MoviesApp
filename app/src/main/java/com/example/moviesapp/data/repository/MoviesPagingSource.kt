package com.example.moviesapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.model.Movie
import javax.inject.Inject

class MoviesPagingSource @Inject constructor(
    private val apiService: ApiService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = apiService.getMovies(page)
            val movies = response.results
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(
                anchorPosition
            )?.prevKey
        }
    }
}