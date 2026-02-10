package com.example.moviesandmore.domain.movie

import junit.framework.TestCase.assertNotNull
import org.junit.Test

class SearchMovieUseCaseTest {

    @Test
    fun shouldBeAbleToSearchForMovieByMovieTitle() {
        val mockUseCase = SearchMovieUseCase()
        val result = mockUseCase.execute("Avengers")
        assertNotNull(result)
    }
}