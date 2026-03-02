package com.example.moviesandmore.domain

import com.apollographql.apollo.api.ApolloResponse
import com.example.moviesandmore.GetCharactersQuery

interface CharacterRepository {
    suspend fun getCharacters(): ApolloResponse<GetCharactersQuery.Data>
}