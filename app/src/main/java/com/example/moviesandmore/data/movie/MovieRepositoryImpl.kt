package com.example.moviesandmore.data.movie

import android.util.Log
import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApiService: MovieApiService,
    private val movieMapper: MovieMapper,
    private val movieDao: MovieDao
) : MovieRepository {
    override suspend fun searchMovieByTitle(movieTitle: String): List<Movie> {
        if (movieTitle.isEmpty()) {
            throw IllegalArgumentException("Invalid input")
        }
        val response = movieApiService.getMoviesByTitle(movieTitle)
        val movieApiResponse = response.body()
        val movieDtos = movieApiResponse?.titles ?: emptyList()
        return movieMapper.toDomainList(movieDtos)
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
        Log.i("MovieRepositoryImpl", "getMovieById: ${movieId}")
        val response = movieApiService.getMovieById(movieId)
        Log.i("MovieRepositoryImpl", "response: ${response}")
        Log.i("MovieRepositoryImpl", "response_body: ${response.body()}")


        if (!response.isSuccessful) {
            throw IllegalArgumentException("Movie not found")
        }

        val movieDetailDto = response.body()
            ?: throw IllegalArgumentException("Movie not found")

        return movieMapper.toDomainFromDetail(movieDetailDto)
    }

    override suspend fun getAllPopularMovies(): List<Movie> {
        val response = movieApiService.getAllPopularMovies()
        val movieApiResponse = response.body()
        val movieDtos = movieApiResponse?.titles ?: emptyList()
        return movieMapper.toDomainList(movieDtos)
    }
}