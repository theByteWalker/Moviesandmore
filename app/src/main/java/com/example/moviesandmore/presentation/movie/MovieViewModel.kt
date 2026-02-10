package com.example.moviesandmore.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.SearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            searchMovieByTitle(query)
        } else {
            _uiState.value = MovieSearchState.Idle
        }
    }

    private fun searchMovieByTitle(movieTitle: String) {
        viewModelScope.launch {
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
}

sealed class MovieSearchState {
    data object Idle : MovieSearchState()
    data object Loading : MovieSearchState()
    data object Empty : MovieSearchState()
    data class Success(val movies: List<Movie>) : MovieSearchState()
    data class Error(val message: String) : MovieSearchState()
}