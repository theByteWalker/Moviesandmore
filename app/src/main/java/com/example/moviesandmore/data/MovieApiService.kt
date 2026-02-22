package com.example.moviesandmore.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("titles")
    suspend fun getTitles(@Query("pageToken") pageToken: String? = null): MovieResponse

    @GET("titles/{titleId}")
    suspend fun getMovieDetails(@Path("titleId") titleId: String): MovieDetailResponse
}