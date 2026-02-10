package com.example.moviesandmore.domain.movie

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SearchMovieUseCaseModule {
    @Provides
    @Singleton
    fun provideSearchMovieUseCase(movieRepository: MovieRepository): SearchMovieUseCase {
        return SearchMovieUseCase(movieRepository)
    }
}