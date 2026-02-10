package com.example.moviesandmore

import retrofit2.http.GET

interface MovieApiService {
    @GET("titles")
    suspend fun getTitles(): MovieResponse
}