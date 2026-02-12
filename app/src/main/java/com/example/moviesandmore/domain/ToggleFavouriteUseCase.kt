package com.example.moviesandmore.domain

import com.example.moviesandmore.data.MovieDetailResponse
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor (
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: MovieDetailResponse): Boolean {
        val isFav = repository.isMovieFavorite(movie.id)
        if (isFav) {
            repository.removeFavorite(movie.id)
            return false
        } else {
            repository.addFavorite(movie)
            return true
        }
    }

    suspend fun isFavorite(titleId: String): Boolean {
        return repository.isMovieFavorite(titleId)
    }
}