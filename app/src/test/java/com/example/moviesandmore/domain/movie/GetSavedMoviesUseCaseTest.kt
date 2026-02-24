package com.example.moviesandmore.domain.movie

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetSavedMoviesUseCaseTest {

    private val mockRepository = mockk<MovieRepository>()
    private val useCase = GetSavedMoviesUseCase(mockRepository)

    @Test
    fun shouldReturnSavedMoviesFlow() = runTest {
        val expectedMovies = listOf(
            Movie("tt1", "Movie 1", null),
            Movie("tt2", "Movie 2", null)
        )
        val expectedFlow = flowOf(expectedMovies)
        
        every { mockRepository.getSavedMovies() } returns expectedFlow

        val resultFlow = useCase.execute()
        val resultList = resultFlow.toList()

        assertEquals(1, resultList.size)
        assertEquals(expectedMovies, resultList.first())
    }

    @Test
    fun shouldBubbleUpExceptionsFromRepository() = runTest {
        val errorMessage = "Database locked"
        val errorFlow = flow<List<Movie>> {
            throw RuntimeException(errorMessage)
        }
        
        every { mockRepository.getSavedMovies() } returns errorFlow

        val resultFlow = useCase.execute()

        val result = runCatching { resultFlow.toList() }
        val exception = result.exceptionOrNull()

        assertTrue(exception is RuntimeException)
        assertEquals(errorMessage, exception?.message)
    }
}
