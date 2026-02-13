package com.example.moviesandmore.domain

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moviesandmore.data.MovieResponse
import com.example.moviesandmore.data.MovieTitle
import com.example.moviesandmore.data.PrimaryImage
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class GetAllTitlesUseCaseTest {

    @Mock
    private lateinit var repository: MovieRepository

    private lateinit var getAllTitlesUseCase: GetAllTitlesUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getAllTitlesUseCase = GetAllTitlesUseCase(repository)
    }

    @Test
    fun getMoviesDataFromRepository() = runBlocking {
        val mockMovies = listOf(
            MovieTitle(id = "1", primaryTitle = "Inception", originalTitle = "Inception", plot = "Dreams", startYear = 2010, rating = null, primaryImage = PrimaryImage("", 0, 0))
        )
        val mockResponse = MovieResponse(titles = mockMovies, totalCount = 1, nextPageToken = null)

        `when`(repository.getMovies()).thenReturn(mockResponse)

        val result = getAllTitlesUseCase()

        assertEquals(1, result.titles.size)
        assertEquals("Inception", result.titles[0].primaryTitle)
    }
}