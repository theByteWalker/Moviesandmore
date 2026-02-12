package com.example.moviesandmore.domain.movie

import javax.inject.Inject

class GetMovieByIdUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: String): Movie {
        return movieRepository.getMovieById(movieId)
    }
}