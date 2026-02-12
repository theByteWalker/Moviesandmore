package com.example.moviesandmore.domain.movie

import com.example.moviesandmore.data.movie.MovieEntity
class SaveMovieUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(movie: Movie): MovieEntity {
        if (movie.name.isEmpty()) {
            throw IllegalArgumentException("Invalid Movie, Cannot Save")
        }
        val savedMovie = movieRepository.saveMovie(movie)
        return savedMovie
    }
}