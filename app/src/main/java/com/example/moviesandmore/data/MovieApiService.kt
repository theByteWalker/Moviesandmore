package com.example.moviesandmore.data

import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiService {
    @GET("titles")
    suspend fun getTitles(): MovieResponse

    @GET("titles/{titleId}")
    suspend fun getMovieDetails(@Path("titleId") titleId: String): MovieDetailResponse
}