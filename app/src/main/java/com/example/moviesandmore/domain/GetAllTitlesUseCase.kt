package com.example.moviesandmore.domain

import android.util.Log
import com.example.moviesandmore.data.MovieDao
import com.example.moviesandmore.data.MovieEntity
import com.example.moviesandmore.data.MovieResponse
import javax.inject.Inject

class GetAllTitlesUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val movieDao: MovieDao
) {
    suspend operator fun invoke(): MovieResponse {
        val movies = repository.getMovies()
        val movieEntities = movies.titles.map { movie ->
            MovieEntity(
                id = movie.id,
                primaryTitle = movie.primaryTitle,
                originalTitle = movie.originalTitle,
                plot = movie.plot,
                startYear = movie.startYear,
                rating = movie.rating?.aggregateRating?.toInt() ?: 0,
                primaryImageUrl = movie.primaryImage.url
            )
        }.first()
//        movieDao.save(movieEntities)
        val allMovies = movieDao.getAll()
        allMovies.forEach {
            Log.i("MoviesAndMore", "$movieEntities")
        }
        return movies
    }
}