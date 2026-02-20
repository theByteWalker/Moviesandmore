package com.example.moviesandmore.domain.movie

import javax.inject.Inject

class GetAllPopularMoviesUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend fun execute(): List<Movie> {
        return movieRepository.getAllPopularMovies()
    }
}