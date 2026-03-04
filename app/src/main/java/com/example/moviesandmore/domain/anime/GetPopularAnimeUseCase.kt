package com.example.moviesandmore.domain.anime

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPopularAnimeUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    fun execute(): Flow<Result<List<Anime>>> = flow {
        emit(animeRepository.getPopularAnime())
    }
}
