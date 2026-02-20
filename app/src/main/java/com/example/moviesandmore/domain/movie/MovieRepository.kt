package com.example.moviesandmore.domain.movie

import com.example.moviesandmore.data.movie.MovieEntity
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
interface MovieRepository {
    suspend fun searchMovieByTitle(movieTitle: String): List<Movie>
    suspend fun saveMovie(movie: Movie): MovieEntity
    fun getSavedMovies(): Flow<List<MovieEntity>>
    suspend fun getMovieById(movieId: String): Movie

    suspend fun getAllPopularMovies(): List<Movie>
    fun getPopularMoviesPager(): Flow<PagingData<Movie>>
}