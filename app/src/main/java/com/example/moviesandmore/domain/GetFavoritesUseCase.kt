package com.example.moviesandmore.domain

import com.example.moviesandmore.data.FavoriteMovieEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<FavoriteMovieEntity>> {
        return repository.getFavorites()
    }
}

