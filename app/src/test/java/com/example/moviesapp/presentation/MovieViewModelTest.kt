package com.example.moviesapp.presentation

import androidx.paging.Pager
import androidx.paging.PagingData
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.domain.GetMovieDetailsUseCase
import com.example.moviesapp.domain.GetMoviesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieViewModelTest {
    private val mockGetMoviesUseCase: GetMoviesUseCase = mockk(relaxed = true)
    private val mockGetMovieDetailsUseCase: GetMovieDetailsUseCase = mockk(relaxed = true)
    private val mockMovie: Movie = mockk()
    private val mockMovieDetails: MovieDetails = mockk()
    private val movieId = 1
    private lateinit var subject: MovieViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        subject = MovieViewModel(
            mockGetMoviesUseCase,
            mockGetMovieDetailsUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun loadMovies_success_returnResponse() = runTest {
        val pagingDataFlow = flow { emit(PagingData.from(listOf(mockMovie, mockMovie))) }
        val mockPager = mockk<Pager<Int, Movie>> { every { flow } returns pagingDataFlow }
        coEvery { mockGetMoviesUseCase() } returns mockPager
        subject.loadMovies()

        coVerify { mockGetMoviesUseCase() }
        val successState = subject.moviesListResponse.value
        assert(successState is UiState.Success)
    }

    @Test
    fun loadMovies_failure_returnError() = runTest {
        val errorMessage = "Network Error"
        coEvery { mockGetMoviesUseCase() } throws Exception(errorMessage)

        subject.loadMovies()

        coVerify { mockGetMoviesUseCase() }
        assert(subject.moviesListResponse.value is UiState.Error)
        assertEquals(UiState.Error(errorMessage), subject.moviesListResponse.value)
    }

    @Test
    fun init_getDetailsById_returnLoadingResult() = runTest {
        assertEquals(UiState.Loading, subject.movieDetailsResponse.value)
    }

    @Test
    fun getDetailsById_success_returnResponse() = runTest {
        coEvery { mockGetMovieDetailsUseCase(movieId) } returns mockMovieDetails

        subject.getDetailsById(movieId)

        coVerify { mockGetMovieDetailsUseCase(movieId) }
        assert(subject.movieDetailsResponse.value is UiState.Success)
        assertEquals(UiState.Success(mockMovieDetails), subject.movieDetailsResponse.value)
    }

    @Test
    fun getDetailsById_failure_returnError() = runTest {
        val errorMessage = "Network Error"
        coEvery { mockGetMovieDetailsUseCase(movieId) } throws Exception(errorMessage)

        subject.getDetailsById(movieId)

        coVerify { mockGetMovieDetailsUseCase(movieId) }
        assert(subject.movieDetailsResponse.value is UiState.Error)
        assertEquals(UiState.Error(errorMessage), subject.movieDetailsResponse.value)
    }

    @Test
    fun onIntent_onLoadMoviesIntent_callLoadMovies() = runTest {
        val pagingDataFlow = flow { emit(PagingData.from(listOf(mockMovie, mockMovie))) }
        val mockPager = mockk<Pager<Int, Movie>> { every { flow } returns pagingDataFlow }
        coEvery { mockGetMoviesUseCase() } returns mockPager

        subject.onIntent(MovieIntent.LoadMovies)

        coVerify { mockGetMoviesUseCase() }
    }

    @Test
    fun onIntent_onGetMovieDetailsIntent_callGetDetailsById() = runTest {
        coEvery { mockGetMovieDetailsUseCase(movieId) } returns mockMovieDetails

        subject.onIntent(MovieIntent.GetMovieDetails(movieId))

        coVerify { mockGetMovieDetailsUseCase(movieId) }
    }
}