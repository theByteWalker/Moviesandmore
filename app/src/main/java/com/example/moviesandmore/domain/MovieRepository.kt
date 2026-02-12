package com.example.moviesandmore.domain

import com.example.moviesandmore.data.MovieDetailResponse
import com.example.moviesandmore.data.MovieResponse

interface MovieRepository {
    suspend fun getMovies(): MovieResponse
    suspend fun getMovieDetails(titleId: String): MovieDetailResponse
    suspend fun isMovieFavorite(titleId: String): Boolean
    suspend fun addFavorite(movie: MovieDetailResponse)
    suspend fun removeFavorite(titleId: String)
}