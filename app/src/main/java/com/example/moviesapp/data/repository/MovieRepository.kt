package com.example.moviesapp.data.repository

import androidx.paging.Pager
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails

interface MovieRepository {

    suspend fun getMovies(): Pager<Int, Movie>

    suspend fun getMovieDetails(id: Int): MovieDetails
}