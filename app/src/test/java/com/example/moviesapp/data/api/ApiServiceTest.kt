package com.example.moviesapp.data.api

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var subject: ApiService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        subject = retrofit.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getMovies_success_returnResponse() = runTest {
        val mockResponse = MockResponse().setBody(generateMoviesResponse(1)).setResponseCode(200)
        mockWebServer.enqueue(mockResponse)

        val response = subject.getMovies(1)

        assertEquals(1, response.page)
        assertEquals(2, response.results.size)
        assertEquals(3, response.total_pages)
        assertEquals(100, response.total_results)
    }

    @Test
    fun getMovies_failure_returnError() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        try {
            subject.getMovies(1)
        } catch (e: Exception) {
            assert(e is retrofit2.HttpException)
            assertEquals(404, (e as retrofit2.HttpException).code())
        }
    }

    @Test
    fun getMovieDetails_success_returnResponse() = runTest {
        mockWebServer.enqueue(
            MockResponse().setBody(generateMovieDetailsResponse(1)).setResponseCode(200)
        )

        val response = subject.getMovieDetails(1)

        assertEquals(1, response.id)
        assertEquals("It Ends with Us", response.title)
        assertEquals("en", response.original_language)
        assertEquals(131, response.runtime)
        assertEquals("Released", response.status)
    }

    @Test
    fun getMovieDetails_failure_returnError() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        try {
            subject.getMovieDetails(1)
        } catch (e: Exception) {
            assert(e is retrofit2.HttpException)
            assertEquals(404, (e as retrofit2.HttpException).code())
        }
    }

    private fun generateMoviesResponse(page: Int): String {
        return """
         {
           "page":$page,
           "results":[
              {
                 "adult":false,
                 "backdrop_path":"/yDHYTfA3R0jFYba16jBB1ef8oIt.jpg",
                 "genre_ids":[
                    28,
                    35,
                    878
                 ],
                 "id":533535,
                 "original_language":"en",
                 "original_title":"Deadpool \u0026 Wolverine",
                 "overview":"A listless Wade Wilson toils away in civilian life with his days as the morally flexible mercenary, Deadpool, behind him. But when his homeworld faces an existential threat, Wade must reluctantly suit-up again with an even more reluctant Wolverine.",
                 "popularity":2225.512,
                 "poster_path":"/8cdWjvZQUExUUTzyp4t6EDMubfO.jpg",
                 "release_date":"2024-07-24",
                 "title":"Deadpool \u0026 Wolverine",
                 "video":false,
                 "vote_average":7.672,
                 "vote_count":3160
              },
              {
                 "adult":false,
                 "backdrop_path":"/hZVIwxi6kK4KnpVPEWwdhG0cMM5.jpg",
                 "genre_ids":[
                    9648,
                    53
                 ],
                 "id":1012148,
                 "original_language":"en",
                 "original_title":"Chapel",
                 "overview":"A man becomes a suspect in a serial murder case after waking from a coma with no recollection of who he is.",
                 "popularity":590.886,
                 "poster_path":"/94sIggRUBioP19m3vJQfV3lq6Z6.jpg",
                 "release_date":"2024-01-17",
                 "title":"Chapel",
                 "video":false,
                 "vote_average":6.233,
                 "vote_count":15
              }
           ],
           "total_pages":3,
           "total_results":100
        }
        """
    }

    private fun generateMovieDetailsResponse(id: Int): String {
        return """
            {
               "adult":false,
               "backdrop_path":"/4TcpeInvAkxXlVWgoX9izD1cndY.jpg",
               "belongs_to_collection":null,
               "budget":25000000,
               "genres":[
                  {
                     "id":10749,
                     "name":"Romance"
                  },
                  {
                     "id":18,
                     "name":"Drama"
                  }
               ],
               "homepage":"https://www.itendswithus.movie",
               "id":$id,
               "imdb_id":"tt10655524",
               "origin_country":[
                  "US"
               ],
               "original_language":"en",
               "original_title":"It Ends with Us",
               "overview":"When a woman's first love suddenly reenters her life, her relationship with a charming, but abusive neurosurgeon is upended, and she realizes she must learn to rely on her own strength to make an impossible choice for her future.",
               "popularity":1794.621,
               "poster_path":"/cSMdFWmajaX4oUMLx7HEDI84GkP.jpg",
               "production_companies":[
                  {
                     "id":144610,
                     "logo_path":"/879WnwsOH1oP6NlLY7ChD6vL6lh.png",
                     "name":"Wayfarer Studios",
                     "origin_country":"US"
                  },
                  {
                     "id":194285,
                     "logo_path":null,
                     "name":"Saks Picture Company",
                     "origin_country":"US"
                  },
                  {
                     "id":5,
                     "logo_path":"/71BqEFAF4V3qjjMPCpLuyJFB9A.png",
                     "name":"Columbia Pictures",
                     "origin_country":"US"
                  }
               ],
               "production_countries":[
                  {
                     "iso_3166_1":"US",
                     "name":"United States of America"
                  }
               ],
               "release_date":"2024-08-07",
               "revenue":334836337,
               "runtime":131,
               "spoken_languages":[
                  {
                     "english_name":"English",
                     "iso_639_1":"en",
                     "name":"English"
                  }
               ],
               "status":"Released",
               "tagline":"We break the pattern or the pattern breaks us.",
               "title":"It Ends with Us",
               "video":false,
               "vote_average":6.87,
               "vote_count":552
            }
        """
    }
}