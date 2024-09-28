package com.example.moviesapp.domain

import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Int): MovieDetails {
        return repository.getMovieDetails(id = id)
    }
}