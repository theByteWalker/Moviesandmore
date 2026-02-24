package com.example.moviesandmore.data.movie

import com.example.moviesandmore.core.utils.Logger
import com.example.moviesandmore.domain.movie.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieModule {


    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieMapper(): MovieMapper {
        return MovieMapper()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        movieApiService: MovieApiService,
        movieMapper: MovieMapper,
        movieDao: MovieDao,
        logger: Logger
    ): MovieRepository {
        return MovieRepositoryImpl(movieApiService, movieMapper, movieDao, logger)
    }
}