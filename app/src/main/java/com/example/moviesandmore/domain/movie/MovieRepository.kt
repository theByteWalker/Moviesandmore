package com.example.moviesandmore.domain.movie

interface MovieRepository {
    fun searchMovieByTitle(movieTitle: String): List<Movie>
}