package com.example.moviesandmore.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.data.MovieDetailResponse
import com.example.moviesandmore.domain.GetMovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.moviesandmore.app.SendNotification
import com.example.moviesandmore.domain.ToggleFavoriteUseCase
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val sendNotification: SendNotification,
    @ApplicationContext private val context: Context
): ViewModel() {
    var uiState by mutableStateOf(MovieDetailState())
        private set
//    private val _movieDetailsState = mutableStateOf<MovieDetailResponse?>(null)
//    val movieDetailsState: State<MovieDetailResponse?> = _movieDetailsState

    fun handleIntent(intent: MovieDetailIntent) {
        when (intent) {
            is MovieDetailIntent.LoadMovie -> fetchMovieDetails(intent.id)
            is MovieDetailIntent.ToggleFavorite -> toggleFavorite(intent.movie)
        }
    }
    fun fetchMovieDetails(titleId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = getMovieDetailsUseCase(titleId)
//                _movieDetailsState.value = response
                val currentlyFavorite = toggleFavoriteUseCase.isFavorite(titleId)
                uiState = uiState.copy(
                    isLoading = false,
                    movie = response,
                    isFavorite = currentlyFavorite
                )
            } catch (e: Exception) {
                Log.e("MoviesAndMore", "Error: ${e.message}")
            }
        }
    }

    private fun toggleFavorite(movie: MovieDetailResponse) {
        viewModelScope.launch {
            val isNowFavorite = toggleFavoriteUseCase(movie)
            uiState = uiState.copy(isFavorite = isNowFavorite)
            if (isNowFavorite) {
                sendNotification.triggerNotification(context, "Movie Notification", "Movie is now added to favorites")
            } else {
                sendNotification.triggerNotification(context, "Movie Notification", "Movie is now removed from favorites")
            }
        }
    }
}