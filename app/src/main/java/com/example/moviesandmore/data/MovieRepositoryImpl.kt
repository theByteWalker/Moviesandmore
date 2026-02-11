package com.example.moviesandmore.data

import com.example.moviesandmore.domain.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService
) : MovieRepository {
    override suspend fun getMovies(): MovieResponse {
        return apiService.getTitles()
    }
}