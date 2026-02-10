package com.example.moviesandmore.data.movie

import com.google.gson.annotations.SerializedName

data class MovieApiResponse(
    @SerializedName("titles")
    val titles: List<MovieDto>?
)

data class MovieDto(
    @SerializedName("primaryTitle")
    val primaryTitle: String,
)
