package com.example.moviesapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.domain.GetMovieDetailsUseCase
import com.example.moviesapp.domain.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
) : ViewModel() {
    private val _moviesListResponse =
        MutableStateFlow<UiState<Flow<PagingData<Movie>>>>(UiState.Loading)
    val moviesListResponse: StateFlow<UiState<Flow<PagingData<Movie>>>> =
        _moviesListResponse.asStateFlow()

    private val _movieDetailsResponse = MutableStateFlow<UiState<MovieDetails>>(UiState.Loading)
    val movieDetailsResponse: StateFlow<UiState<MovieDetails>> = _movieDetailsResponse.asStateFlow()

    init {
        onIntent(MovieIntent.LoadMovies)
    }

    fun onIntent(intent: MovieIntent) {
        when (intent) {
            is MovieIntent.LoadMovies -> loadMovies()
            is MovieIntent.GetMovieDetails -> getDetailsById(intent.id)
        }
    }

    fun loadMovies() {
        viewModelScope.launch {
            _moviesListResponse.value = UiState.Loading
            try {
                val response = getMoviesUseCase().flow.cachedIn(viewModelScope)
                _moviesListResponse.value = UiState.Success(response)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error loading movies: ${e.message}", e)
                _moviesListResponse.value = UiState.Error("Failed to load movies. Please try again.")
            }
        }
    }

    fun getDetailsById(id: Int) {
        viewModelScope.launch {
            _movieDetailsResponse.value = UiState.Loading
            try {
                val response = getMovieDetailsUseCase(id)
                _movieDetailsResponse.value = UiState.Success(response)
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error loading movies: ${e.message}", e)
                _movieDetailsResponse.value = UiState.Error("Failed to fetch movie details. Please try again.")
            }
        }
    }
}