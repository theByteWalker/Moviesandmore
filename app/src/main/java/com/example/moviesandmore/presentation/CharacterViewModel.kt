package com.example.moviesandmore.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.domain.GetAllCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val getCharactersUseCase: GetAllCharactersUseCase
) : ViewModel() {

    private val _state = mutableStateOf(CharacterState())
    val state: State<CharacterState> = _state

    fun handleIntent(intent: CharacterIntent) {
        when (intent) {
            is CharacterIntent.LoadCharacters -> getCharacters()
        }
    }

    private fun getCharacters() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val response = getCharactersUseCase()
                val results = response.data?.characters?.results?.filterNotNull() ?: emptyList()
                _state.value = _state.value.copy(
                    isLoading = false,
                    characters = results,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }
}
