package com.example.moviesapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.db.MovieDao
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {
    private val mockApiService: ApiService = mockk()
    private val mockMovieDao: MovieDao = mockk()

    @OptIn(ExperimentalPagingApi::class)
    private val mockMovieRemoteMediator: MovieRemoteMediator = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    private var mockTestDispatcher = UnconfinedTestDispatcher()
    private val mockMovieDetails: MovieDetails = mockk()
    private val mockPagingSource: PagingSource<Int, Movie> = mockk()
    private lateinit var subject: MovieRepositoryImpl

    @OptIn(ExperimentalPagingApi::class)
    @Before
    fun setUp() {
        subject = MovieRepositoryImpl(
            mockApiService,
            mockMovieDao,
            mockMovieRemoteMediator,
            mockTestDispatcher
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun getMovies_callMovieDaoGetAll() = runTest {
        coEvery { mockMovieDao.getAll() } returns mockPagingSource

        val pager = subject.getMovies()

        assertNotNull(pager)
        coEvery { mockMovieDao.getAll() }
    }

    @Test
    fun getMovieDetails_whenCacheAvailable_returnsCachedDetails() = runTest {
        val movieId = 1
        val cachedDetails = mockMovieDetails
        coEvery { mockMovieDao.getDetailsById(movieId) } returns cachedDetails

        val result = subject.getMovieDetails(movieId)

        assertEquals(cachedDetails, result)
        coVerify(exactly = 0) { mockApiService.getMovieDetails(any()) }
    }

    @Test
    fun getMovieDetails_whenCacheAvailable_returnsFromAPI_andAddCache() = runTest {
        val movieId = 2
        val apiDetails = mockMovieDetails
        coEvery { mockMovieDao.getDetailsById(movieId) } returns null
        coEvery { mockApiService.getMovieDetails(movieId) } returns apiDetails
        coEvery { mockMovieDao.insertDetails(apiDetails) } returns Unit

        val result = subject.getMovieDetails(movieId)

        assertEquals(apiDetails, result)
        coVerify { mockMovieDao.insertDetails(apiDetails) }
        coVerify(exactly = 1) { mockApiService.getMovieDetails(movieId) }
    }
}