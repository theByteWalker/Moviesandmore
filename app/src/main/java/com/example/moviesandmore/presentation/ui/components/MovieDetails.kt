package com.example.moviesandmore.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.moviesandmore.domain.MovieDetailIntent
import com.example.moviesandmore.presentation.MovieDetailsViewModel

@Composable
fun MovieDetails(titleId: String, viewModel: MovieDetailsViewModel = hiltViewModel()) {
    val state = viewModel.uiState
    LaunchedEffect(titleId) {
//        viewModel.fetchMovieDetails(titleId)
        viewModel.handleIntent(MovieDetailIntent.LoadMovie(titleId))
    }
    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(88.dp))
            AsyncImage(
                model = state.movie?.primaryImage?.url,
                contentDescription = null,
                modifier = Modifier
                    .height(300.dp)
                    .aspectRatio(2f / 3f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = state.movie?.primaryTitle ?: "",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                IconButton(onClick = {
                    state.movie?.let {
                        viewModel.handleIntent(MovieDetailIntent.ToggleFavorite(it))
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = if (state.isFavorite) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${state.movie?.rating?.aggregateRating}", style = MaterialTheme.typography.bodyMedium)

                Text(text = " • ", modifier = Modifier.padding(horizontal = 8.dp))

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Release Date",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = state.movie?.startYear?.toString() ?: "----", style = MaterialTheme.typography.bodyMedium)

                Text(text = " • ", modifier = Modifier.padding(horizontal = 8.dp))

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Duration",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "${state.movie?.runtimeSeconds?.div(60)} min", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Synopsis",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = state.movie?.plot ?: "No synopsis available.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (state.movie != null && state.movie.directors.isNotEmpty()) {
                val directorsList = state.movie.directors.joinToString(separator = ", ") { it.displayName }

                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Directors",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = directorsList,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (state.movie != null && state.movie.stars.isNotEmpty()) {
                val castList = state.movie.stars.joinToString(separator = ", ") { it.displayName }

                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Cast",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = castList,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}