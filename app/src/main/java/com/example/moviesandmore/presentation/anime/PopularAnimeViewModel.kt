package com.example.moviesandmore.presentation.anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.core.presentation.UiState
import com.example.moviesandmore.domain.anime.Anime
import com.example.moviesandmore.domain.anime.GetPopularAnimeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PopularAnimeViewModel @Inject constructor(
    private val getPopularAnimeUseCase: GetPopularAnimeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Anime>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<Anime>>> = _uiState.asStateFlow()

    init {
        loadPopularAnime()
    }

    private fun loadPopularAnime() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getPopularAnimeUseCase.execute()
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Unknown Error")
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { animeList ->
                            _uiState.value = UiState.Success(animeList)
                        },
                        onFailure = { error ->
                            _uiState.value = UiState.Error(error.message ?: "Failed to fetch anime")
                        }
                    )
                }
        }
    }
}
