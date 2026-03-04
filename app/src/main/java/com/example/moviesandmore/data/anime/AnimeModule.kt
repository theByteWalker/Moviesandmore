package com.example.moviesandmore.data.anime

import com.apollographql.apollo.ApolloClient
import com.example.moviesandmore.core.utils.Logger
import com.example.moviesandmore.domain.anime.AnimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnimeModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://graphql.anilist.co")
            .build()
    }

    @Provides
    @Singleton
    fun provideAnimeMapper(): AnimeMapper {
        return AnimeMapper()
    }

    @Provides
    @Singleton
    fun provideAnimeRepository(
        apolloClient: ApolloClient,
        mapper: AnimeMapper,
        logger: Logger
    ): AnimeRepository {
        return AnimeRepositoryImpl(apolloClient, mapper, logger)
    }
}
