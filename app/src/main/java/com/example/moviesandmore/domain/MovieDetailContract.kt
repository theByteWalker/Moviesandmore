package com.example.moviesandmore.domain

import com.example.moviesandmore.data.MovieDetailResponse

sealed class MovieDetailIntent {
    data class LoadMovie(val id: String) : MovieDetailIntent()
    data class ToggleFavorite(val movie: MovieDetailResponse) : MovieDetailIntent()
}

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: MovieDetailResponse? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)