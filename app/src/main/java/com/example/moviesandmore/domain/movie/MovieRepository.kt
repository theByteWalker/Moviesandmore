package com.example.moviesandmore.domain.movie

interface MovieRepository {
    suspend fun searchMovieByTitle(movieTitle: String): List<Movie>
}