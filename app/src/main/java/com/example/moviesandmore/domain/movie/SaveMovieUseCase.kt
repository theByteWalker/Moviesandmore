package com.example.moviesandmore.domain.movie

import javax.inject.Inject

class SaveMovieUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    suspend fun execute(movie: Movie): Movie {
        if (movie.name.isEmpty()) {
            throw IllegalArgumentException("Invalid Movie, Cannot Save")
        }
        val savedMovie = movieRepository.saveMovie(movie)
        return savedMovie
    }
}