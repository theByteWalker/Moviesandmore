package com.example.moviesandmore.domain.movie

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test

class GetMovieByIdUseCaseTest {

    private val mockRepository = mockk<MovieRepository>()
    private val useCase = GetMovieByIdUseCase(mockRepository)

    @Test
    fun shouldReturnMovieWhenIdIsValid() = runTest {
        val expectedMovie = Movie("tt27497448", "Avengers", null)
        coEvery { mockRepository.getMovieById("tt27497448") } returns expectedMovie

        val result = useCase.execute("tt27497448")

        assertEquals(expectedMovie, result)
    }

    @Test
    fun shouldThrowExceptionWhenIdIsEmpty() {
        val errorResponse = "Invalid movie ID"

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runTest {
                useCase.execute("")
            }
        }
        assertEquals(errorResponse, exception.message)
    }
}
