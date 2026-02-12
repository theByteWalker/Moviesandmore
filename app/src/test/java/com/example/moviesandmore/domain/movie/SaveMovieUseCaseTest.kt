package com.example.moviesandmore.domain.movie

import com.example.moviesandmore.data.movie.MovieEntity
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test

class SaveMovieUseCaseTest {

    val mockRepository = mockk<MovieRepository>()
    val useCase = SaveMovieUseCase(mockRepository)

    @Test
    fun shouldBeAbleToSaveAValidMovie() = runTest {
        val movie = Movie("tt27497448", "Avengers", null)
        val movieEntiy = MovieEntity(1, "tt27497448", "Avengers", null)
        coEvery { mockRepository.saveMovie(movie) } returns movieEntiy

        val result = useCase.execute(movie)

        assertEquals(movieEntiy, result)
    }

    @Test
    fun shouldReturnFalseIfMovieIsInvalid() {
        val movie = Movie("tt27497448", "", null)
        val errorResponse = "Invalid Movie, Cannot Save"

        val exception = assertThrows(IllegalArgumentException::class.java) {
            runTest { useCase.execute(movie) }
        }

        assertEquals(errorResponse, exception.message)
    }
}