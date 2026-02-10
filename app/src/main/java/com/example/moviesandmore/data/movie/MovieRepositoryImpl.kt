package com.example.moviesandmore.data.movie

import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val movieApiService: MovieApiService, private val movieMapper: MovieMapper) : MovieRepository {
    override fun searchMovieByTitle(movieTitle: String): List<Movie>{
        if (movieTitle.isEmpty()) {
            throw IllegalArgumentException("Invalid input")
        }
        val response = movieApiService.getMoviesByTitle(movieTitle)
        val movieApiResponse = response.body()
        val movieDtos = movieApiResponse?.titles ?: emptyList()
        return movieMapper.toDomainList(movieDtos)
    }
}