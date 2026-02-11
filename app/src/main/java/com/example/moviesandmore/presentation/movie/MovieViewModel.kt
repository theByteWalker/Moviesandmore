package com.example.moviesandmore.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.SearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<MovieSearchState>(MovieSearchState.Idle)
    val uiState: StateFlow<MovieSearchState> = _uiState.asStateFlow()

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500) // Wait 500ms after user stops typing
                .distinctUntilChanged() // Only emit if query actually changed
                .collect { query ->
                    if (query.isEmpty()) {
                        _uiState.value = MovieSearchState.Idle
                    } else {
                        searchMovieByTitle(query)
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchMovieByTitle(movieTitle: String) {
        _uiState.value = MovieSearchState.Loading
        try {
            val movies = searchMovieUseCase.execute(movieTitle)
            _uiState.value = if (movies.isEmpty()) {
                MovieSearchState.Empty
            } else {
                MovieSearchState.Success(movies)
            }
        } catch (e: Exception) {
            _uiState.value = MovieSearchState.Error(
                e.message ?: "An unknown error occurred"
            )
        }
    }
}

sealed class MovieSearchState {
    data object Idle : MovieSearchState()
    data object Loading : MovieSearchState()
    data object Empty : MovieSearchState()
    data class Success(val movies: List<Movie>) : MovieSearchState()
    data class Error(val message: String) : MovieSearchState()
}