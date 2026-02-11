package com.example.moviesandmore.domain.movie

import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class SaveMovieUseCaseTest {

    val mockRepository = mockk<MovieRepository>()
    val useCase = SaveMovieUseCase(mockRepository)

    @Test
    fun shouldBeAbleToSaveAValidMovie() {
        val movie = Movie("Avengers", null)
        every { mockRepository.saveMovie(movie) } returns movie

        val result = useCase.execute(movie)

        assertEquals(movie, result)
    }

    @Test
    fun shouldReturnFalseIfMovieIsInvalid() {
        val movie = Movie("", null)
        val errorResponse = "Invalid Movie, Cannot Save"

        val exception = assertThrows(IllegalArgumentException::class.java) {
            useCase.execute(movie)
        }

        assertEquals(errorResponse, exception.message)
    }
}