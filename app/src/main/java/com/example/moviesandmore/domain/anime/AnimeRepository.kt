package com.example.moviesandmore.domain.anime

interface AnimeRepository {
    suspend fun getPopularAnime(): Result<List<Anime>>
}
