package com.example.moviesandmore.domain.movie

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    fun execute(): Flow<List<Movie>> {
        return movieRepository.getSavedMovies()
    }
}
