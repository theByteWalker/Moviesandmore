package com.example.moviesandmore.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviesandmore.presentation.PopularMoviesViewModel

@Composable
fun PopularMovies(innerPaddingValues: PaddingValues, onClick: (String) -> Unit, viewModel: PopularMoviesViewModel = hiltViewModel()) {
//    val movies = viewModel.uiState.value
    val movies = viewModel.moviePagingFlow.collectAsLazyPagingItems()
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(innerPaddingValues),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count = movies.itemCount) { index ->
            val movie = movies[index]
            if (movie != null) {
                MovieCard(movie = movie, onClick = onClick)
            }
        }
    }
}