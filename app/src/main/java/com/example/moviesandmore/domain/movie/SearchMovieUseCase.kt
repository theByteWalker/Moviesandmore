package com.example.moviesandmore.domain.movie

class SearchMovieUseCase(private val movieRepository: MovieRepository){
    suspend fun execute(movieTitle: String): List<Movie> {
        if (movieTitle.isEmpty()) {
            throw IllegalArgumentException("Invalid input")
        }
        return movieRepository.searchMovieByTitle(movieTitle)
    }
}