package com.example.moviesandmore.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.moviesandmore.presentation.MyViewModel

@Composable
fun PopularMovies(innerPaddingValues: PaddingValues, viewModel: MyViewModel = hiltViewModel()) {
    val movies = viewModel.uiState.value

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(innerPaddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(movies) { movie ->
            MovieCard(movie)
        }
    }
}