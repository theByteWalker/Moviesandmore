package com.example.moviesandmore.data.movie

import com.google.gson.annotations.SerializedName

data class MovieApiResponse(
    @SerializedName("titles")
    val titles: List<MovieDto>?,
    @SerializedName("nextPageToken")
    val nextPageToken: String? = null
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

