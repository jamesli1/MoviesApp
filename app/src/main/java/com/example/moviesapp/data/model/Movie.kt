package com.example.moviesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val poster_path: String,
    val release_date: String
)