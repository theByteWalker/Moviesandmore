package com.example.moviesandmore.presentation.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.app.FavouritesNotificationService
import com.example.moviesandmore.core.presentation.UiState
import com.example.moviesandmore.core.utils.Logger
import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.domain.movie.SaveMovieUseCase
import com.example.moviesandmore.domain.movie.SearchMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val searchMovieUseCase: SearchMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
    private val favouritesNotificationService: FavouritesNotificationService,
    private val logger: Logger
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<List<Movie>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Movie>>> = _uiState.asStateFlow()

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
                        _uiState.value = UiState.Idle
                    } else {
                        searchMovieByTitle(query)
                    }
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun saveMovie(movie: Movie) {
        viewModelScope.launch {
            try {
                saveMovieUseCase.execute(movie)
                favouritesNotificationService.showMovieAddedNotification(movie.name)
            } catch (e: Exception) {
                logger.e("MovieViewModel", "Error saving movie", e)
            }
        }
    }

    private suspend fun searchMovieByTitle(movieTitle: String) {
        _uiState.value = UiState.Loading
        try {
            val movies = searchMovieUseCase.execute(movieTitle)
            _uiState.value = if (movies.isEmpty()) {
                UiState.Empty
            } else {
                UiState.Success(movies)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error(
                e.message ?: "An unknown error occurred"
            )
        }
    }
}
