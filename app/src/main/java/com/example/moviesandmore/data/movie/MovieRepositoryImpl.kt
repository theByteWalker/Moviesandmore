package com.example.moviesandmore.data.movie

import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.MovieRepository

class MovieRepositoryImpl: MovieRepository {
    override fun searchMovieByTitle(movieTitle: String): List<Movie>{
        return listOf(Movie("Avengers"), Movie("Avenger: Endgame"))
    }
}