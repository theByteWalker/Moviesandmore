package com.example.moviesandmore.domain

import javax.inject.Inject

class GetAllTitlesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke() = repository.getMovies()
}