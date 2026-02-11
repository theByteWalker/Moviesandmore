package com.example.moviesandmore.data.movie

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MovieRepositoryImplTest {

    private lateinit var movieApiService: MovieApiService
    private lateinit var movieMapper: MovieMapper
    private lateinit var movieRepository: MovieRepositoryImpl

    @Before
    fun setup() {
        movieApiService = mockk()
        movieMapper = MovieMapper()
        movieRepository = MovieRepositoryImpl(movieApiService, movieMapper)
    }

    @Test
    fun givenAValidMovieTitle_whenSearchMovieByTitle_thenReturnMovieList() = runTest {
        val movieDtos = listOf(
            MovieDto(primaryTitle = "Avengers", primaryImage = null),
            MovieDto(primaryTitle = "Avenger: Endgame", primaryImage = null)
        )
        val apiResponse = MovieApiResponse(titles = movieDtos)
        val response = Response.success(apiResponse)
        
        coEvery { movieApiService.getMoviesByTitle("Avengers") } returns response

        val result = movieRepository.searchMovieByTitle("Avengers")

        assertEquals(2, result.size)
        assertEquals("Avengers", result[0].name)
        assertEquals("Avenger: Endgame", result[1].name)
    }

    @Test
    fun givenAnInvalidMovieTitle_whenSearchMovieByTitle_thenThrowIllegalArgumentException() {
        val errorResponse = "Invalid input"

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runTest {
                movieRepository.searchMovieByTitle("")
            }
        }

        assertEquals(errorResponse, exception.message)
    }
    
    @Test
    fun givenNullTitlesInResponse_whenSearchMovieByTitle_thenReturnEmptyList() = runTest {
        val apiResponse = MovieApiResponse(titles = null)
        val response = Response.success(apiResponse)
        
        coEvery { movieApiService.getMoviesByTitle("Unknown") } returns response

        val result = movieRepository.searchMovieByTitle("Unknown")

        assertEquals(0, result.size)
    }
}