package com.example.moviesandmore.domain

import androidx.paging.PagingData
import com.example.moviesandmore.data.MovieDetailResponse
import com.example.moviesandmore.data.MovieResponse
import com.example.moviesandmore.data.MovieTitle
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
//    suspend fun getMovies(): MovieResponse
    fun getMoviesPaging(): Flow<PagingData<MovieTitle>>
    suspend fun getMovieDetails(titleId: String): MovieDetailResponse
    suspend fun isMovieFavorite(titleId: String): Boolean
    suspend fun addFavorite(movie: MovieDetailResponse)
    suspend fun removeFavorite(titleId: String)
}