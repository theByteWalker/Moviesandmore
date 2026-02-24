package com.example.moviesandmore.data.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviesandmore.core.network.ApiResult
import com.example.moviesandmore.core.network.safeApiCall
import com.example.moviesandmore.core.utils.Logger
import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieMapper: MovieMapper,
    private val movieDao: MovieDao,
    private val logger: Logger
) : MovieRepository {
    override suspend fun searchMovieByTitle(movieTitle: String): List<Movie> {
        if (movieTitle.isEmpty()) {
            throw IllegalArgumentException("Invalid input")
        }
        val result = safeApiCall { movieApiService.getMoviesByTitle(movieTitle) }
        return when (result) {
            is ApiResult.Success -> {
                val movieDtos = result.data.titles ?: emptyList()
                movieMapper.toDomainList(movieDtos)
            }
            is ApiResult.Error -> throw Exception("API Error: ${result.message}")
            is ApiResult.Exception -> throw result.throwable
        }
    }

    override suspend fun saveMovie(movie: Movie): MovieEntity {
        val movieEntityToSave = movieMapper.toEntity(movie)
        val insertedId = movieDao.save(movieEntityToSave)
        return movieDao.getById(insertedId) ?: throw IllegalStateException("Failed to retrieve saved movie")
    }

    override fun getSavedMovies(): Flow<List<MovieEntity>> {
        return movieDao.getAll()
    }

    override suspend fun getMovieById(movieId: String): Movie {
        val result = safeApiCall { movieApiService.getMovieById(movieId) }
        return when (result) {
            is ApiResult.Success -> movieMapper.map(result.data)
            is ApiResult.Error -> throw IllegalArgumentException("Movie not found: ${result.message}")
            is ApiResult.Exception -> throw result.throwable
        }
    }

    override suspend fun getAllPopularMovies(): List<Movie> {
        val result = safeApiCall { movieApiService.getAllPopularMovies() }
        return when (result) {
            is ApiResult.Success -> movieMapper.toDomainList(result.data.titles ?: emptyList())
            is ApiResult.Error -> throw Exception("API Error: ${result.message}")
            is ApiResult.Exception -> throw result.throwable
        }
    }

    override fun getPopularMoviesPager(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                initialLoadSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { PopularMoviesPagingSource(movieApiService, movieMapper, logger) }
        ).flow
    }
}