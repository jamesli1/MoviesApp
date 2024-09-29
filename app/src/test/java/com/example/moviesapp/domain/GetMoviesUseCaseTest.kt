package com.example.moviesapp.domain

import androidx.paging.Pager
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.repository.MovieRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GetMoviesUseCaseTest {
    private val mockRepository: MovieRepository = mockk()
    private val mockPager: Pager<Int, Movie> = mockk()
    private lateinit var subject: GetMoviesUseCase

    @Before
    fun setUp() {
        subject = GetMoviesUseCase(mockRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke_getMovies() = runTest {
        coEvery { mockRepository.getMovies() } returns mockPager
        subject.invoke()

        coVerify { mockRepository.getMovies() }
    }
}