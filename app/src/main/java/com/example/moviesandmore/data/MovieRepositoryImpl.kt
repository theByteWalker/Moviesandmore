package com.example.moviesandmore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesandmore.domain.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val favMoviesDao: FavoriteDao
) : MovieRepository {
//    override suspend fun getMovies(): MovieResponse {
//        return apiService.getTitles()
//    }
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

    override fun getMoviesPaging(): Flow<PagingData<MovieTitle>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiService) }
        ).flow
    }

    override fun getFavorites(): Flow<List<FavoriteMovieEntity>> {
        return favMoviesDao.getAllFavorites()
    }
}