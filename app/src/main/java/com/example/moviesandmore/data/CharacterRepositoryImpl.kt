package com.example.moviesandmore.data

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloResponse
import com.example.moviesandmore.GetCharactersQuery
import com.example.moviesandmore.domain.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : CharacterRepository {
    override suspend fun getCharacters(): ApolloResponse<GetCharactersQuery.Data> {
        return apolloClient.query(GetCharactersQuery()).execute()
    }
}