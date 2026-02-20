package com.example.moviesandmore.presentation.popularmovies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.domain.movie.GetAllPopularMoviesUseCase
import com.example.moviesandmore.domain.movie.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PopularMoviesState {
    data object Loading : PopularMoviesState()
    data class Success(val movies: List<Movie>) : PopularMoviesState()
    data class Error(val message: String) : PopularMoviesState()
}

@HiltViewModel
class PopularMoviesViewModel @Inject constructor(
    private val getAllPopularMoviesUseCase: GetAllPopularMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PopularMoviesState>(PopularMoviesState.Loading)
    val uiState: StateFlow<PopularMoviesState> = _uiState.asStateFlow()

    init {
        loadPopularMovies()
    }

    private fun loadPopularMovies() {
        viewModelScope.launch {
            try {
                val movies = getAllPopularMoviesUseCase.execute()
                _uiState.value = if (movies.isEmpty()) {
                    PopularMoviesState.Error("No popular movies found")
                } else {
                    PopularMoviesState.Success(movies)
                }
            } catch (e: Exception) {
                _uiState.value = PopularMoviesState.Error(
                    e.message ?: "An unknown error occurred"
                )
            }
        }
    }
}
