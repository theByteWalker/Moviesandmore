package com.example.moviesandmore.domain

import com.example.moviesandmore.data.MovieDetailResponse
import javax.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(titleId: String): MovieDetailResponse {
        return repository.getMovieDetails(titleId)
    }
}