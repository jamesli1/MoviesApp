package com.example.moviesapp.presentation

sealed class MovieIntent {
    data object LoadMovies : MovieIntent()
    data class GetMovieDetails(val id: Int) : MovieIntent()
}