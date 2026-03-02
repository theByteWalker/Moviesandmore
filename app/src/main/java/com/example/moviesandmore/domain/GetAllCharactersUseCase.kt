package com.example.moviesandmore.domain

import com.apollographql.apollo.api.ApolloResponse
import com.example.moviesandmore.GetCharactersQuery
import javax.inject.Inject

class GetAllCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(): ApolloResponse<GetCharactersQuery.Data> {
        return repository.getCharacters()
    }
}