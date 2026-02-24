package com.example.moviesandmore.domain.movie

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
interface MovieRepository {
    suspend fun searchMovieByTitle(movieTitle: String): List<Movie>
    suspend fun saveMovie(movie: Movie): Movie
    fun getSavedMovies(): Flow<List<Movie>>
    suspend fun getMovieById(movieId: String): Movie

    suspend fun getAllPopularMovies(): List<Movie>
    fun getPopularMoviesPager(): Flow<PagingData<Movie>>
}