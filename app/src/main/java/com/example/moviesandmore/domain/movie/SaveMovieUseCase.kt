package com.example.moviesandmore.domain.movie

class SaveMovieUseCase(private val movieRepository: MovieRepository) {
    fun execute(movie: Movie): Movie {
        if (movie.name.isEmpty()) {
            throw IllegalArgumentException("Invalid Movie, Cannot Save")
        }
        val savedMovie = movieRepository.saveMovie(movie)
        return savedMovie
    }
}