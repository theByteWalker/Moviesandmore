package com.example.moviesandmore.data.movie

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("search/titles")
    suspend fun getMoviesByTitle(@Query("query") movieTitle: String): Response<MovieApiResponse>

    @GET("titles/{id}")
    suspend fun getMovieById(@Path("id") movieId: String): Response<MovieDetailDto>

    @GET("titles")
    suspend fun getAllPopularMovies(
        @Query("pageToken") pageToken: String? = null
    ): Response<MovieApiResponse>
}