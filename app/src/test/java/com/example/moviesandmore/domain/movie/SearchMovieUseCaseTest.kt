package com.example.moviesandmore.domain.movie

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Test

class SearchMovieUseCaseTest {
    val useCase = SearchMovieUseCase()

    @Test
    fun shouldBeAbleToSearchForMovieByMovieTitle() {
        val result = useCase.execute("Avengers")
        assertNotNull(result)
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