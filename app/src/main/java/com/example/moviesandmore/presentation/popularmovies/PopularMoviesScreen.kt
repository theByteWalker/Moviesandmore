package com.example.moviesandmore.presentation.popularmovies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.moviesandmore.domain.movie.Movie
import com.example.moviesandmore.core.ui.components.TopBar

@Composable
fun PopularMoviesScreen(
    viewModel: PopularMoviesViewModel = hiltViewModel()
) {
    val lazyPagingItems = viewModel.moviesPagingFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = { TopBar("Popular Movies") }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                lazyPagingItems.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                lazyPagingItems.loadState.refresh is LoadState.Error -> {
                    val error = (lazyPagingItems.loadState.refresh as LoadState.Error).error
                    Text(
                        text = error.message ?: "An unknown error occurred",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(lazyPagingItems.itemCount) { index ->
                            val movie = lazyPagingItems[index]
                            if (movie != null) {
                                MovieCard(movie = movie)
                            }
                        }

                        if (lazyPagingItems.loadState.append is LoadState.Loading) {
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = "${movie.name} poster",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
