package com.example.moviesandmore.data

import retrofit2.http.GET

interface MovieApiService {
    @GET("titles")
    suspend fun getTitles(): MovieResponse
}