package com.example.moviesapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapp.data.model.Movie
import com.example.moviesapp.data.model.MovieDetails
import com.example.moviesapp.data.model.MovieRemoteKeys

@Database(entities = [Movie::class, MovieDetails::class, MovieRemoteKeys::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao
}