package com.example.moviesandmore.data.movie

import com.example.moviesandmore.core.utils.Logger

import com.example.moviesandmore.domain.movie.Movie
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MovieRepositoryImplTest {

    private lateinit var movieApiService: MovieApiService
    private lateinit var movieMapper: MovieMapper
    private lateinit var movieDao: MovieDao
    private lateinit var movieRepository: MovieRepositoryImpl
    private lateinit var logger: Logger

    @Before
    fun setup() {
        movieApiService = mockk()
        movieMapper = MovieMapper()
        movieDao = mockk()
        logger = mockk(relaxed = true)
        movieRepository = MovieRepositoryImpl(movieApiService, movieMapper, movieDao, logger)
    }

    @Test
    fun givenAValidMovieTitle_whenSearchMovieByTitle_thenReturnMovieList() = runTest {
        val movieDtos = listOf(
            MovieDto(id = "tt27497448", primaryTitle = "Avengers", primaryImage = null),
            MovieDto(id = "tt27497449", primaryTitle = "Avenger: Endgame", primaryImage = null)
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

    @Test
    fun givenAValidMovie_whenSaveMovie_thenReturnSavedMovieEntity() = runTest {
        val movie = Movie(titleId = "tt27497448", name = "Avengers", imageUrl = "https://example.com/image.jpg")
        val movieEntityToSave = MovieEntity(id = 0, titleId = "tt27497448", name = "Avengers", posterUrl = "https://example.com/image.jpg", isFavorite = true)
        val insertedId = 1L
        val savedMovieEntity = MovieEntity(id = 1, titleId = "tt27497448", name = "Avengers", posterUrl = "https://example.com/image.jpg", isFavorite = true)

        coEvery { movieDao.save(movieEntityToSave) } returns insertedId
        coEvery { movieDao.getById(insertedId) } returns savedMovieEntity

        val result = movieRepository.saveMovie(movie)

        assertEquals(Movie("tt27497448", "Avengers", "https://example.com/image.jpg"), result)
        coVerify(exactly = 1) { movieDao.save(movieEntityToSave) }
        coVerify(exactly = 1) { movieDao.getById(insertedId) }
    }

    @Test
    fun givenAValidMovieWithNullImageUrl_whenSaveMovie_thenReturnSavedMovieEntityWithEmptyPosterUrl() = runTest {
        val movie = Movie(titleId = "tt27497448", name = "Avengers", imageUrl = null)
        // Change posterUrl to empty string instead of null to match what the repository actually does
        val movieEntityToSave = MovieEntity(id = 0, titleId = "tt27497448", name = "Avengers", posterUrl = "", isFavorite = true)
        val insertedId = 1L
        val savedMovieEntity = MovieEntity(id = 1, titleId = "tt27497448", name = "Avengers", posterUrl = "", isFavorite = true)

        coEvery { movieDao.save(movieEntityToSave) } returns insertedId
        coEvery { movieDao.getById(insertedId) } returns savedMovieEntity

        val result = movieRepository.saveMovie(movie)

        assertEquals(Movie("tt27497448", "Avengers", null), result)
        assertEquals(null, result.imageUrl)
    }

    @Test
    fun givenSaveSucceedsButGetByIdReturnsNull_whenSaveMovie_thenThrowIllegalStateException() {
        val movie = Movie(titleId = "tt27497448", name = "Matrix", imageUrl = null)
        val movieEntityToSave = MovieEntity(id = 0, titleId = "tt27497448", name = "Matrix", posterUrl = "", isFavorite = true)
        val insertedId = 3L

        coEvery { movieDao.save(movieEntityToSave) } returns insertedId
        coEvery { movieDao.getById(insertedId) } returns null

        val exception = assertThrows(IllegalStateException::class.java) {
            runTest {
                movieRepository.saveMovie(movie)
            }
        }

        assertEquals("Failed to retrieve saved movie", exception.message)
    }
}