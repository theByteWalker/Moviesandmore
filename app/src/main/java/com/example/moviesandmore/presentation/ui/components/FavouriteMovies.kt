package com.example.moviesandmore.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.moviesandmore.data.FavoriteMovieEntity
import com.example.moviesandmore.presentation.FavouritesViewModel

@Composable
fun FavouritesMovies(
    innerPaddingValues: PaddingValues,
    onClick: (String) -> Unit,
    viewModel: FavouritesViewModel = hiltViewModel()
) {
    val favoriteMovies = viewModel.favoriteMovies.value

    if (favoriteMovies.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "No Favorites Yet",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Add movies to your favorites to see them here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(favoriteMovies) { movie ->
                FavoriteMovieCard(favorite = movie, onClick = onClick)
            }
        }
    }
}

@Composable
fun FavoriteMovieCard(favorite: FavoriteMovieEntity, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .clickable { onClick(favorite.id) }
        ) {
            AsyncImage(
                model = favorite.imageUrl,
                contentDescription = favorite.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = favorite.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 20.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = Bold
            )
            favorite.year?.let {
                Text(
                    text = "$it",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            favorite.rating?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "★ $it/10",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

