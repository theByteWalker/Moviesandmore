package com.example.moviesandmore.data.anime

import com.apollographql.apollo.ApolloClient
import com.example.moviesandmore.core.utils.Logger
import com.example.moviesandmore.data.anime.graphql.PopularAnimeQuery
import com.example.moviesandmore.domain.anime.Anime
import com.example.moviesandmore.domain.anime.AnimeRepository
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val mapper: AnimeMapper,
    private val logger: Logger
) : AnimeRepository {

    override suspend fun getPopularAnime(): Result<List<Anime>> {
        return try {
            val response = apolloClient.query(PopularAnimeQuery()).execute()
            
            if (response.hasErrors()) {
                val errorMessage = response.errors?.first()?.message ?: "Unknown GraphQL Error"
                logger.e("AnimeRepositoryImpl", "GraphQL error: $errorMessage")
                Result.failure(Exception(errorMessage))
            } else if (response.data != null) {
                val mappedData = mapper.map(response.data!!)
                Result.success(mappedData)
            } else {
                logger.e("AnimeRepositoryImpl", "Empty data from server")
                Result.failure(Exception("No data found"))
            }
        } catch (e: Exception) {
            logger.e("AnimeRepositoryImpl", "Network/Exception: ${e.message}")
            Result.failure(e)
        }
    }
}
