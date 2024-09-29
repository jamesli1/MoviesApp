package com.example.moviesapp.domain

import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetMovieDetailsUseCaseTest {
    private val mockRepository: MovieRepository = mockk()
    private val mockMovieDetails: MovieDetails = mockk()
    private val id = 1
    private lateinit var subject: GetMovieDetailsUseCase

    @Before
    fun setUp() {
        subject = GetMovieDetailsUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke_getMovies() = runTest {
        coEvery { mockRepository.getMovieDetails(id) } returns mockMovieDetails
        subject.invoke(id)

        coVerify { mockRepository.getMovieDetails(id) }
    }
}