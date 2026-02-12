package com.example.moviesandmore.data

import com.example.moviesandmore.domain.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val favMoviesDao: FavoriteDao
) : MovieRepository {
    override suspend fun getMovies(): MovieResponse {
        return apiService.getTitles()
    }
    override suspend fun getMovieDetails(titleId: String): MovieDetailResponse {
        return apiService.getMovieDetails(titleId)
    }

    override suspend fun isMovieFavorite(titleId: String): Boolean {
        return favMoviesDao.isFavorite(titleId)
    }

    override suspend fun addFavorite(movie: MovieDetailResponse) {
        val entity = FavoriteMovieEntity(
            id = movie.id,
            title = movie.primaryTitle,
            imageUrl = movie.primaryImage?.url,
            year = movie.startYear,
            rating = movie.rating?.aggregateRating?.toInt()
        )
        favMoviesDao.insertFavorite(entity)
    }

    override suspend fun removeFavorite(titleId: String) {
        favMoviesDao.deleteFavoriteById(titleId)
    }
}