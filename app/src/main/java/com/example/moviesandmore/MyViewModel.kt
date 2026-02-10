package com.example.moviesandmore

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val apiService: MovieApiService,
    private val savedStateHandle: SavedStateHandle // Hilt injects this automatically
) : ViewModel() {
    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                val response = apiService.getTitles()
                Log.d("MovieRepo", "Successfully fetched ${response.titles.size} movies")

            } catch (e: Exception) {
                Log.e("MovieRepo", "Error: ${e.message}")
            }
        }
    }
}