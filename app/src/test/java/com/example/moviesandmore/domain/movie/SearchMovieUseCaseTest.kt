package com.example.moviesandmore.domain.movie

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test

class SearchMovieUseCaseTest {

    val mockRepository = mockk<MovieRepository>()
    val useCase = SearchMovieUseCase(mockRepository)

    @Test
    fun shouldBeAbleToSearchForMovieByMovieTitle() = runTest {
        val expectedMovieList: List<Movie> = listOf(Movie("Avengers"), Movie("Avenger: Endgame"))
        coEvery { mockRepository.searchMovieByTitle("Avengers") } returns expectedMovieList

        val result = useCase.execute("Avengers")

        assertEquals(expectedMovieList, result)
    }

    @Test
    fun shouldReturnErrorIfInputStringISNotValid() {
        val errorResponse = "Invalid input"

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runTest {
                useCase.execute("")
            }
        }
        assertEquals(errorResponse, exception.message)
    }
}