package com.example.moviesapp.data.api

import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("popular")
    suspend fun getMovies(
        @Query("page") pageNumber: Int
    ): MoviesResponse

    @GET("{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int
    ): MovieDetails
}