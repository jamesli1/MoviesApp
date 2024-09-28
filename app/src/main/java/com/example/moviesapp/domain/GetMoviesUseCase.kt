package com.example.moviesapp.domain

import androidx.paging.Pager
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(private val repository: MovieRepository) {

    suspend operator fun invoke(): Pager<Int, Movie> {
        return repository.getMovies()
    }
}