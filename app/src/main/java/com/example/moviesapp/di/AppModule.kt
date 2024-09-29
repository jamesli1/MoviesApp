package com.example.moviesapp.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import com.example.moviesapp.data.api.ApiService
import com.example.moviesapp.data.db.MovieDao
import com.example.moviesapp.data.db.MovieDatabase
import com.example.moviesapp.data.repository.MovieRemoteMediator
import com.example.moviesapp.data.repository.MovieRepository
import com.example.moviesapp.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

class AppModule {

    @Provides
    @Singleton
    internal fun provideDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(
            application,
            MovieDatabase::class.java,
            "MovieDB.db"
        ).allowMainThreadQueries().build()
    }

    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Provides
    fun provideMovieRepository(
        api: ApiService,
        movieDao: MovieDao,
        movieRemoteMediator: MovieRemoteMediator
    ): MovieRepository {
        return MovieRepositoryImpl(api, movieDao, movieRemoteMediator)
    }
}
