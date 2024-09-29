package com.example.moviesapp.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MoviesResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoviesPagingSourceTest {
    private val mockApiService: ApiService = mockk()
    private val mockMovie: Movie = mockk()
    private val mockMoviesResponse: MoviesResponse = mockk(relaxed = true)
    private lateinit var subject: MoviesPagingSource

    @Before
    fun setup() {
        subject = MoviesPagingSource(mockApiService)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun load_successful_returnsPage() = runTest {
        val mockMovies = listOf(mockMovie, mockMovie)
        val mockResponse = mockk<MoviesResponse> {
            coEvery { results } returns mockMovies
        }

        coEvery { mockApiService.getMovies(1) } returns mockResponse

        val result = subject.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertEquals(
            PagingSource.LoadResult.Page(
                data = mockMovies,
                prevKey = null,
                nextKey = 2
            ),
            result
        )
    }

    @Test
    fun load_failure_returnsError() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { mockApiService.getMovies(any()) } throws exception

        val result = subject.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assert(result is PagingSource.LoadResult.Error)
        assertEquals("Network error", (result as PagingSource.LoadResult.Error).throwable.message)
    }

    @Test
    fun getRefreshKey_returnsCorrectKey() {
        val state = PagingState(
            pages = listOf(
                PagingSource.LoadResult.Page(
                    data = listOf(
                        Movie(
                            id = 1,
                            title = "Movie 1",
                            poster_path = "",
                            release_date = ""
                        )
                    ),
                    prevKey = 1,
                    nextKey = 3
                )
            ),
            anchorPosition = 2,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        val result = subject.getRefreshKey(state)

        assertEquals(1, result)
    }
}