package com.example.moviesandmore.domain.movie

import androidx.paging.PagingData
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetPopularMoviesPagerUseCaseTest {

    private val mockRepository = mockk<MovieRepository>()
    private val useCase = GetPopularMoviesPagerUseCase(mockRepository)

    @Test
    fun shouldReturnPagingDataFlow() = runTest {
        val mockPagingData = PagingData.empty<Movie>()
        val expectedFlow = flowOf(mockPagingData)

        every { mockRepository.getPopularMoviesPager() } returns expectedFlow

        val resultFlow = useCase.invoke()
        val resultList = resultFlow.toList()

        assertEquals(1, resultList.size)
        assertEquals(mockPagingData, resultList.first())
    }

    @Test
    fun shouldBubbleUpExceptionsFromRepository() = runTest {
        val errorMessage = "Network Error"
        val errorFlow = flow<PagingData<Movie>> {
            throw RuntimeException(errorMessage)
        }

        every { mockRepository.getPopularMoviesPager() } returns errorFlow

        val resultFlow = useCase.invoke()

        val result = runCatching { resultFlow.toList() }
        val exception = result.exceptionOrNull()
        
        assertTrue(exception is RuntimeException)
        assertEquals(errorMessage, exception?.message)
    }
}
