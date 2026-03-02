package com.example.moviesandmore.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.data.FavoriteMovieEntity
import com.example.moviesandmore.domain.GetFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {
    private val _favoriteMoviesState = mutableStateOf<List<FavoriteMovieEntity>>(emptyList())
    val favoriteMovies: State<List<FavoriteMovieEntity>> = _favoriteMoviesState

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                _favoriteMoviesState.value = favorites
            }
        }
    }
}

