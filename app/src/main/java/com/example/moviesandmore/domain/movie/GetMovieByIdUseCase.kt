package com.example.moviesandmore.domain.movie

class GetMovieByIdUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: String): Movie {
        return movieRepository.getMovieById(movieId)
    }
}