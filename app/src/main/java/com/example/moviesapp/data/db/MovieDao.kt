package com.example.moviesapp.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movie: List<Movie>)

    @Query("SELECT * from movie ORDER BY release_date DESC")
    fun getAll(): PagingSource<Int, Movie>

    @Query("DELETE FROM movie")
    suspend fun clearAll()

    @Query("SELECT * FROM movie_details WHERE id = :id")
    suspend fun getDetailsById(id: Int): MovieDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDetails(movieDetails: MovieDetails)
}