package com.example.moviesandmore.domain.movie

import com.example.moviesandmore.data.movie.MovieEntity
import kotlinx.coroutines.flow.Flow

class GetSavedMoviesUseCase(private val movieRepository: MovieRepository) {
    fun execute(): Flow<List<MovieEntity>> {
        return movieRepository.getSavedMovies()
    }
}
