package com.example.moviesandmore.data.movie

import com.google.gson.annotations.SerializedName

data class MovieApiResponse(
    @SerializedName("titles")
    val titles: List<MovieDto>?
)

data class MovieDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("primaryTitle")
    val primaryTitle: String,
    @SerializedName("primaryImage")
    val primaryImage: PrimaryImage?
)

data class PrimaryImage(
    @SerializedName("url")
    val url: String?
)

data class MovieDetailDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("primaryTitle")
    val primaryTitle: String,
    @SerializedName("primaryImage")
    val primaryImage: PrimaryImage?
)

