package com.example.moviesandmore.presentation

import com.example.moviesandmore.GetCharactersQuery

sealed class CharacterIntent {
    object LoadCharacters : CharacterIntent()
}

data class CharacterState(
    val isLoading: Boolean = false,
    val characters: List<GetCharactersQuery.Result> = emptyList(),
    val error: String? = null
)