package com.example.moviesandmore.data.movie

import com.example.moviesandmore.domain.movie.Movie
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MovieRepositoryImplTest {

    private val movieRepository = MovieRepositoryImpl()

    @Test
    fun givenAValidMovieTitle_whenSearchMovieByTitle_thenReturnMovieList() {
        val movieList = listOf(Movie("Avengers"), Movie("Avenger: Endgame"))

        val result = movieRepository.searchMovieByTitle("Avengers")

        assertEquals(movieList, result)
    }
}