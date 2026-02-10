package com.example.moviesandmore.domain.movie

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SearchMovieUseCaseTest {

    val mockRepository = mockk<MovieRepository>()
    val useCase = SearchMovieUseCase(mockRepository)

    @Test
    fun shouldBeAbleToSearchForMovieByMovieTitle() {
        val expectedMovieList: List<Movie> = listOf(Movie("Avengers"), Movie("Avenger: Endgame"))
        every { mockRepository.searchMovieByTitle("Avengers") } returns expectedMovieList

        val result = useCase.execute("Avengers")

        assertEquals(expectedMovieList, result)
    }

    @Test
    fun shouldReturnErrorIfInputStringISNotValid() {
        val erroResponse = "Invalid input"

        val exception = assertThrows(IllegalArgumentException::class.java) {
            useCase.execute("")
        }
        assertEquals(erroResponse, exception.message)
    }
}