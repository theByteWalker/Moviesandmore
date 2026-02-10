package com.example.moviesandmore.domain.movie

class SearchMovieUseCase{
    fun execute(movieTitle: String): Unit {
        if (movieTitle.isEmpty()) {
            throw IllegalArgumentException("Invalid input")
        }
        return Unit
    }
}