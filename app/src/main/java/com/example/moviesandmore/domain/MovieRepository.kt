package com.example.moviesandmore.domain

import com.example.moviesandmore.data.MovieResponse

interface MovieRepository {
    suspend fun getMovies(): MovieResponse
}