package com.example.moviesandmore.presentation

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.data.MovieTitle
import com.example.moviesandmore.domain.GetAllTitlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val getAllTitlesUseCase: GetAllTitlesUseCase,
) : ViewModel() {
    private val _movieTitlesState = mutableStateOf<List<MovieTitle>>(emptyList())
    val uiState: State<List<MovieTitle>> = _movieTitlesState

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val response = getAllTitlesUseCase()
                _movieTitlesState.value = response.titles
            } catch (e: Exception) {
                Log.e("MoviesAndMore", "Error: ${e.message}")
            }
        }
    }
}