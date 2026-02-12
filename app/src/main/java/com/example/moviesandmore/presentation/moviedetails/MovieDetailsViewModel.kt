package com.example.moviesandmore.presentation.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesandmore.domain.movie.GetMovieByIdUseCase
import com.example.moviesandmore.domain.movie.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieByIdUseCase: GetMovieByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId: String = savedStateHandle.get<String>("movieId") ?: throw IllegalArgumentException("Missing user ID")
    private val _movie = MutableStateFlow<Movie?>(null)
    val movie: StateFlow<Movie?> = _movie.asStateFlow()

    init {
        getMovieById(movieId)
    }

    private fun getMovieById(movieId: String) {
        viewModelScope.launch {
            val movie = getMovieByIdUseCase.execute(movieId)
            _movie.value = movie
        }
    }
}