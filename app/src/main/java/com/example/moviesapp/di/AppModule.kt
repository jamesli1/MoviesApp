package com.example.moviesapp.di

import com.example.moviesapp.data.api.ApiService
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

    @Singleton
    @Provides
    fun provideMovieRepository(api: ApiService): MovieRepository {
        return MovieRepositoryImpl(api)
    }
}
