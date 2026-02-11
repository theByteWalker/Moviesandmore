package com.example.moviesandmore.data

data class MovieResponse(
    val titles: List<MovieTitle>,
    val totalCount: Int,
    val nextPageToken: String?
)

data class MovieTitle(
    val id: String,
    val primaryTitle: String,
    val originalTitle: String?,
    val plot: String?,
    val startYear: Int?,
    val rating: MovieRating?
)

data class MovieRating(
    val aggregateRating: Double,
    val voteCount: Int
)